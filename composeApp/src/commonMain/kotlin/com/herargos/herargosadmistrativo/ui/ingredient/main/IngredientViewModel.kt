package com.herargos.herargosadmistrativo.ui.ingredient.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.core.ResponseMessage
import com.herargos.herargosadmistrativo.core.utils.Messages
import com.herargos.herargosadmistrativo.core.utils.isError
import com.herargos.herargosadmistrativo.core.utils.isErrorDouble
import com.herargos.herargosadmistrativo.domain.model.Ingredient
import com.herargos.herargosadmistrativo.domain.repository.IngredientRepository
import com.herargos.herargosadmistrativo.domain.usecase.CreateIngredientUseCase
import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientEvents.OnBack
import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientEvents.OnDismiss
import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientEvents.OnDismissDelete
import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientEvents.OnDisplay
import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientEvents.OnDisplayDelete
import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientEvents.OnNameChanged
import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientEvents.OnPriceChanged
import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientEvents.OnStockChanged
import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientEvents.OnSubmitIngredient
import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientEvents.OnUnitChanged
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.creation_ingredient_successful
import herargosadministrativo.composeapp.generated.resources.delete_ingredient_successful
import herargosadministrativo.composeapp.generated.resources.update_ingredient_successful
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IngredientViewModel(
    private val onBack: () -> Unit,
    private val repository: IngredientRepository,
    private val messages: Messages,
    private val createIngredientUseCase: CreateIngredientUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(IngredientState())
    val state: StateFlow<IngredientState> = _state

    init {
        viewModelScope.launch {
            repository.getIngredients().collect { result ->
                when (result) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            state.copy(
                                isError = true,
                            )
                        }
                        messages.sendMessage(result.error)
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            state.copy(
                                ingredients = result.data
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

    fun onEvent(event: IngredientEvents) {
        when (event) {
            OnBack -> navigateToBack()
            OnDismiss -> onDismiss()
            is OnDisplay -> onDisplay(event.ingredient)
            is OnNameChanged -> onNameChanged(event.text)
            is IngredientEvents.OnValidateName -> validateName()
            is OnStockChanged -> onStockChanged(event.text)
            is IngredientEvents.OnValidateStock -> validateStock()
            is OnUnitChanged -> onUnitChanged(event.text)
            is IngredientEvents.OnValidateUnit -> validateUnit()
            is OnPriceChanged -> onPriceChanged(event.text)
            is IngredientEvents.OnValidatePrice -> validatePrice()
            OnSubmitIngredient -> onSubmitIngredient()
            OnDismissDelete -> onDismissDelete()
            is OnDisplayDelete -> onDisplayDelete(event.ingredient)
            is IngredientEvents.OnConfirmDelete -> onConfirmDelete(event.ingredient)
            is IngredientEvents.OnUpdateIngredient -> onUpdateIngredient(event.ingredient)
            is IngredientEvents.OnImageChanged -> imageChanged(event.imageData)
        }
    }

    private fun imageChanged(imageData: ByteArray) {
        _state.update { it.copy(imageData = imageData) }
    }

    private fun onUpdateIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isLoading = true
                )
            }
            when (val result = createIngredientUseCase(ingredient, _state.value.imageData)) {
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
                            isSuccess = true,
                            ingredient = Ingredient()
                        )
                    }
                    delay(1000)
                    _state.update { state ->
                        state.copy(
                            isSuccess = false,
                            showDialog = false
                        )
                    }
                    messages.sendMessage(ResponseMessage(Res.string.update_ingredient_successful))
                }

                else -> {}
            }
        }
    }

    private fun onConfirmDelete(ingredient: Ingredient) {
        viewModelScope.launch {
            when (val result = repository.deleteIngredient(ingredient)) {
                is RequestState.Error -> {
                    _state.update { state ->
                        state.copy(
                            showDialogDelete = false
                        )
                    }
                    messages.sendMessage(result.error)
                }

                is RequestState.Success -> {
                    _state.update { state ->
                        state.copy(
                            showDialogDelete = false
                        )
                    }
                    messages.sendMessage(ResponseMessage(Res.string.delete_ingredient_successful))
                }

                else -> {}
            }
        }
    }

    private fun onDisplayDelete(ingredient: Ingredient) {
        _state.update { state ->
            state.copy(showDialogDelete = true, ingredientSelected = ingredient)
        }
    }

    private fun onDismissDelete() {
        _state.update { state ->
            state.copy(showDialogDelete = false)
        }
    }

    private fun onDisplay(ingredient: Ingredient?) {
        _state.update { state ->
            state.copy(
                showDialog = true,
                ingredient = ingredient ?: Ingredient(),
                inputError = false,
                inputsFill = false
            )
        }
    }

    private fun onDismiss() {
        _state.update { state ->
            state.copy(
                showDialog = false,
                ingredient = Ingredient(),
                imageData = null
            )
        }
    }


    private fun navigateToBack() {
        onBack()
    }

    private fun allInputsFill() {
        if (_state.value.ingredient.name.isNotEmpty() &&
            _state.value.ingredient.stock.isNotEmpty() &&
            _state.value.ingredient.unit.isNotEmpty() &&
            _state.value.ingredient.price.isNotEmpty()
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

    private fun validateName() {
        _state.update { state ->
            val error = state.ingredient.name.isError()
            val allErrors =
                (error != null) || (state.errorStock != null) || (state.errorUnit != null) || (state.errorPrice != null)
            state.copy(errorName = error, inputError = allErrors)
        }
    }

    private fun validateStock() {
        _state.update { state ->
            val error = state.ingredient.stock.isErrorDouble()
            val allErrors =
                (state.errorName != null) || (error != null) || (state.errorUnit != null) || (state.errorPrice != null)
            state.copy(errorStock = error, inputError = allErrors)
        }
    }

    private fun validateUnit() {
        _state.update { state ->
            val error = state.ingredient.unit.isError()
            val allErrors =
                (state.errorName != null) || (state.errorStock != null) || (error != null) || (state.errorPrice != null)
            state.copy(errorUnit = error, inputError = allErrors)
        }
    }

    private fun validatePrice() {
        _state.update { state ->
            val error = state.ingredient.price.isErrorDouble()
            val allErrors =
                (state.errorName != null) || (state.errorStock != null) || (state.errorUnit != null) || (error != null)
            state.copy(errorPrice = error, inputError = allErrors)
        }
    }

    // Métodos onXChanged solo actualizan el texto, no validan
    private fun onNameChanged(text: String) {
        _state.update { it.copy(ingredient = it.ingredient.copy(name = text)) }
        validateName()
        allInputsFill()
    }

    private fun onPriceChanged(text: String) {
        _state.update { it.copy(ingredient = it.ingredient.copy(price = text)) }
        validatePrice()
        allInputsFill()
    }

    private fun onStockChanged(text: String) {
        _state.update { it.copy(ingredient = it.ingredient.copy(stock = text)) }
        validateStock()
        allInputsFill()
    }

    private fun onUnitChanged(text: String) {
        _state.update { it.copy(ingredient = it.ingredient.copy(unit = text)) }
        validateUnit()
        allInputsFill()
    }

    private fun onSubmitIngredient() {
        validateName()
        validateStock()
        validateUnit()
        validatePrice()

        if (!state.value.inputError && state.value.inputsFill) {
            viewModelScope.launch {
                _state.update { state ->
                    state.copy(
                        isLoading = true
                    )
                }
                when (val result =
                    createIngredientUseCase(_state.value.ingredient, _state.value.imageData)) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            state.copy(
                                isLoading = false,
                                isError = true
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
                                isSuccess = true,
                                ingredient = Ingredient(),
                                imageData = null
                            )
                        }
                        delay(1000)
                        _state.update { state ->
                            state.copy(
                                isSuccess = false,
                                showDialog = false
                            )
                        }
                        messages.sendMessage(ResponseMessage(Res.string.creation_ingredient_successful))
                    }

                    else -> {}
                }
            }
        }
    }
}