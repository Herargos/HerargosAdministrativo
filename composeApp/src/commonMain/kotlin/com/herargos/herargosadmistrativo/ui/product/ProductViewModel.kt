package com.herargos.herargosadmistrativo.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.core.ResponseMessage
import com.herargos.herargosadmistrativo.core.utils.Messages
import com.herargos.herargosadmistrativo.core.utils.isError
import com.herargos.herargosadmistrativo.core.utils.isErrorDouble
import com.herargos.herargosadmistrativo.domain.model.Ingredient
import com.herargos.herargosadmistrativo.domain.model.Product
import com.herargos.herargosadmistrativo.domain.model.Recipe
import com.herargos.herargosadmistrativo.domain.repository.IngredientRepository
import com.herargos.herargosadmistrativo.domain.repository.ProductRepository
import com.herargos.herargosadmistrativo.domain.usecase.CreateProductUseCase
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.creation_product_successful
import herargosadministrativo.composeapp.generated.resources.delete_product_successful
import herargosadministrativo.composeapp.generated.resources.error_quantity_not_available
import herargosadministrativo.composeapp.generated.resources.update_product_successful
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(
    private val onBack: () -> Unit,
    private val ingredientRepository: IngredientRepository,
    private val productRepository: ProductRepository,
    private val messages: Messages,
    private val createProductUseCase: CreateProductUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProductState())
    val state: StateFlow<ProductState> = _state

    init {
        getLocalProducts()
        getLocalIngredients()
    }

    private fun getLocalIngredients() {
        viewModelScope.launch {
            ingredientRepository.getIngredients().collect { result ->
                when (result) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            state.copy(
                                isError = true,
                                textSnackBar = result.error.message
                            )
                        }
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            val ingredientIdsInRecipe = state.recipe.map { it.idIngredient }.toSet()
                            state.copy(
                                ingredientsDB = result.data.filter { ingredient ->
                                    ingredient.id !in ingredientIdsInRecipe
                                }
                            )
                        }
                    }

                    else -> {
                        _state.update { state ->
                            state.copy(
                                isError = false,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getLocalProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect { result ->
                when (result) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            state.copy(
                                isError = true,
                                textSnackBar = result.error.message
                            )
                        }
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            state.copy(
                                products = result.data
                            )
                        }
                    }

                    else -> {
                        _state.update { state ->
                            state.copy(
                                isError = false,
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: ProductEvents) {
        when (event) {
            ProductEvents.OnBack -> navigateToBack()
            is ProductEvents.OnConfirmDelete -> onDelete(event.product)
            ProductEvents.OnDismiss -> onDismiss()
            ProductEvents.OnDismissDelete -> onDismissDelete()
            is ProductEvents.OnDisplay -> onDisplay(event.product)
            is ProductEvents.OnDisplayDelete -> onDisplayDelete(event.product)

            // Eventos que solo actualizan el texto
            is ProductEvents.OnNameChance -> onNameChance(event.text)
            is ProductEvents.OnStockChance -> onStockChance(event.text)
            is ProductEvents.OnPriceChance -> onPriceChance(event.text)
            is ProductEvents.OnChanceQuantityIngredient -> changeQuantity(event.quantity)

            // Eventos para la validación al perder el foco
            ProductEvents.OnValidateName -> validateName()
            ProductEvents.OnValidateStock -> validateStock()
            ProductEvents.OnValidatePrice -> validatePrice()
            ProductEvents.OnValidateQuantity -> validateQuantity()

            ProductEvents.OnSubmit -> onSubmit()
            is ProductEvents.OnUpdate -> onUpdate(/*event.product*/)
            is ProductEvents.OnSearchItem -> onSearch(event.text)
            is ProductEvents.OnChangeEnablePrice -> onChangeEnablePrice(event.check)
            ProductEvents.OnDismissList -> onDismissList()
            ProductEvents.OnDisplayList -> onDisplayList()
            is ProductEvents.OnSelectedIngredient -> selectedIngredient(event.ingredient)
            ProductEvents.OnAddList -> addList()
            is ProductEvents.OnDeleteItemList -> deleteItemList(event.recipe)
            ProductEvents.OnDismissDetail -> onDismissDetail()
            is ProductEvents.OnDisplayDetail -> onDisplayDetail(event.product)
            is ProductEvents.OnImageChanged -> imageChanged(event.imageData)
        }
    }

    private fun imageChanged(imageData: ByteArray) {
        _state.update { it.copy(imageData = imageData) }
    }

    private fun onDelete(product: Product) {
        viewModelScope.launch {
            when (val result = productRepository.deleteProduct(product)) {
                is RequestState.Error -> {
                    messages.sendMessage(result.error)
                }

                is RequestState.Success -> {
                    messages.sendMessage(ResponseMessage(Res.string.delete_product_successful))
                    _state.update { state ->
                        state.copy(productSelected = product, showDialog = false)
                    }
                }

                else -> {
                    _state.update { state ->
                        state.copy(isLoading = true)
                    }
                }
            }
        }
    }

    private fun onUpdate() {
        // Valida todos los campos antes de enviar
        validateName()
        validateStock()
        validatePrice()
        // Si no hay errores, se ejecuta la lógica de envío
        if (!state.value.inputError && state.value.inputsFill) {
            viewModelScope.launch {
                val product = _state.value.product.copy(
                    name = _state.value.product.name,
                    stock = _state.value.product.stock,
                    price = (if (!_state.value.enablePrice) _state.value.recipe.sumOf { it.price } else _state.value.product.price).toString(),
                    recipes = _state.value.recipe
                )
                when (val result = createProductUseCase(product, _state.value.imageData)) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            state.copy(
                                isLoading = false,
                                isError = true,
                            )
                        }
                        delay(1000)
                        _state.update { state ->
                            state.copy(
                                isError = false
                            )
                        }
                        messages.sendMessage(result.error)
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            state.copy(
                                isLoading = false,
                                isSuccess = true
                            )
                        }
                        delay(1000)
                        _state.update { state ->
                            state.copy(
                                isSuccess = false,
                                showDialog = false
                            )
                        }
                        messages.sendMessage(ResponseMessage(Res.string.update_product_successful))
                    }

                    else -> {
                        _state.update { state ->
                            state.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }

    private fun onDisplayDetail(product: Product) {
        _state.update { state ->
            state.copy(
                showDialogDetail = true,
                productSelected = product
            )
        }
    }

    private fun onDismissDetail() {
        _state.update { state ->
            state.copy(
                showDialogDetail = false
            )
        }
    }

    private fun onSubmit() {
        // Valida todos los campos antes de enviar
        validateName()
        validateStock()
        validatePrice()
        //validateQuantity()

        // Si no hay errores, se ejecuta la lógica de envío
        if (!state.value.inputError && state.value.inputsFill) {
            viewModelScope.launch {
                val product = Product(
                    name = _state.value.product.name,
                    stock = _state.value.product.stock,
                    price = (if (!_state.value.enablePrice) _state.value.recipe.sumOf { it.price } else _state.value.product.price).toString(),
                    recipes = _state.value.recipe
                )
                when (val result = createProductUseCase(product, _state.value.imageData)) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            state.copy(
                                isLoading = false,
                                isError = true,
                            )
                        }
                        delay(1000)
                        _state.update { state ->
                            state.copy(
                                isError = false
                            )
                        }
                        messages.sendMessage(result.error)
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            state.copy(
                                isLoading = false,
                                isSuccess = true
                            )
                        }
                        delay(1000)
                        _state.update { state ->
                            state.copy(
                                isSuccess = false,
                                showDialog = false
                            )
                        }
                        messages.sendMessage(ResponseMessage(Res.string.creation_product_successful))
                    }

                    else -> {}
                }
            }
        }
    }

    private fun deleteItemList(ingredient: Recipe) {
        val newRecipe = _state.value.recipe.toMutableList()
        newRecipe.remove(ingredient)
        _state.update { state ->
            state.copy(recipe = newRecipe)
        }
        getLocalIngredients()
    }

    private fun addList() {
        val newList = _state.value.recipe.toMutableList()
        newList.add(
            Recipe(
                idIngredient = _state.value.ingredientSelected.id,
                name = _state.value.ingredientSelected.name,
                quantity = _state.value.ingredientQuantity.toDouble(),
                price = _state.value.ingredientPrice.toDouble()
            )
        )
        _state.update { state ->
            val price = if (state.enablePrice) 0.0 else newList.sumOf { it.price }
            val newProduct = state.product.copy(price = price.toString())
            state.copy(
                recipe = newList,
                ingredientQuantity = "",
                ingredientPrice = "",
                searchText = "",
                product = newProduct
            )
        }
        getLocalIngredients()
        allInputsFill()
    }

    private fun changeQuantity(quantity: String) {
        if (_state.value.ingredientSelected.id != 0) {
            _state.update { state ->
                val value = try {
                    if (quantity.isNotEmpty()) {
                        (state.ingredientSelected.price.toDouble() * quantity.toDouble()).toString()
                    } else {
                        ""
                    }
                } catch (e: NumberFormatException) {
                    ""
                }
                state.copy(
                    ingredientQuantity = quantity,
                    ingredientPrice = value,
                    errorQuantity = null // Reset error on change, validation will happen on focus change
                )
            }
        }
        validateQuantity()
    }

    private fun selectedIngredient(ingredient: Ingredient) {
        _state.update { state ->
            state.copy(
                ingredientSelected = ingredient,
                enableQuantity = true
            )
        }
    }

    private fun onDisplayList() {
        _state.update { state ->
            state.copy(showDialogList = true)
        }
    }

    private fun onDismissList() {
        _state.update { state ->
            state.copy(showDialogList = false)
        }
    }

    private fun onChangeEnablePrice(check: Boolean) {
        _state.update { state ->
            val price = if (check) 0.0 else state.recipe.sumOf { it.price }
            val newProduct = state.product.copy(price = price.toString())
            state.copy(
                enablePrice = check,
                product = newProduct,
                errorPrice = if (check) state.product.price.isErrorDouble() else null
            )
        }
        updateInputError()
    }

    private fun onSearch(text: String) {
        _state.update { state ->
            state.copy(searchText = text)
        }
    }

    private fun onDisplay(product: Product?) {
        _state.update { state ->
            state.copy(
                showDialog = true,
                product = product ?: Product(),
                recipe = product?.recipes ?: listOf(),
                recipeAux = product?.recipes ?: listOf(),
            )
        }
        getLocalIngredients()
    }

    private fun onDisplayDelete(product: Product) {
        _state.update { state ->
            state.copy(showDialogDelete = true, productSelected = product)
        }
    }

    private fun onDismissDelete() {
        _state.update { state ->
            state.copy(showDialogDelete = false)
        }
    }

    private fun onDismiss() {
        _state.update { state ->
            state.copy(showDialog = false)
        }
    }


    private fun navigateToBack() {
        onBack()
    }

    // Métodos de validación individuales
    private fun validateName() {
        _state.update { state ->
            state.copy(errorName = state.product.name.isError())
        }
        updateInputError()
    }

    private fun validateStock() {
        _state.update { state ->
            state.copy(errorStock = state.product.stock.isErrorDouble())
        }
        updateInputError()
    }

    private fun validatePrice() {
        _state.update { state ->
            val error = if (state.enablePrice) state.product.price.isErrorDouble() else null
            state.copy(errorPrice = error)
        }
        updateInputError()
    }

    private fun validateQuantity() {//Todo: tequedate aqui, ya resolviste como evitar el error de validacion, ahora hay que ver como guardar
        _state.update { state ->
            val suma =
                state.recipeAux.find { it.idIngredient == state.ingredientSelected.id }?.quantity
                    ?: 0.0
            val error =
                try {
                    if (state.ingredientQuantity.toDouble() > (state.ingredientSelected.stock.toDouble() + suma)) {
                        Res.string.error_quantity_not_available
                    } else {
                        state.ingredientQuantity.isErrorDouble()
                    }
                } catch (e: Exception) {
                    state.ingredientQuantity.isErrorDouble()
                }
            state.copy(errorQuantity = error)
        }
        updateInputError()
    }

    // Métodos onXChance que solo actualizan el texto
    private fun onNameChance(text: String) {
        _state.update { it.copy(product = it.product.copy(name = text)) }
        validateName()
        allInputsFill()
    }

    private fun onPriceChance(text: String) {
        _state.update { it.copy(product = it.product.copy(price = text)) }
        validatePrice()
        allInputsFill()
    }

    private fun onStockChance(text: String) {
        _state.update { it.copy(product = it.product.copy(stock = text)) }
        validateStock()
        allInputsFill()
    }

    // Método para actualizar el estado de error general
    private fun updateInputError() {
        _state.update { state ->
            val allErrors =
                (state.errorName != null) || (state.errorStock != null) || (state.errorPrice != null) || (state.errorQuantity != null)
            state.copy(inputError = allErrors)
        }
    }

    private fun allInputsFill() {
        if (_state.value.product.name.isNotEmpty() &&
            _state.value.product.stock.isNotEmpty() &&
            _state.value.recipe.isNotEmpty() &&
            _state.value.product.price.isNotEmpty()
        ) {
            _state.update { state ->
                state.copy(
                    inputsFill = true
                )
            }
        } else {
            _state.update { state ->
                state.copy(
                    inputsFill = false
                )
            }
        }
    }
}