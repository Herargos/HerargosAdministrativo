package com.herargos.herargosadmistrativo.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.core.ResponseMessage
import com.herargos.herargosadmistrativo.core.utils.Messages
import com.herargos.herargosadmistrativo.core.utils.dateTimeNow
import com.herargos.herargosadmistrativo.core.utils.isError
import com.herargos.herargosadmistrativo.core.utils.isErrorDouble
import com.herargos.herargosadmistrativo.core.utils.isErrorInt
import com.herargos.herargosadmistrativo.domain.model.Client
import com.herargos.herargosadmistrativo.domain.model.Product
import com.herargos.herargosadmistrativo.domain.model.Sale
import com.herargos.herargosadmistrativo.domain.model.SaleLine
import com.herargos.herargosadmistrativo.domain.repository.ClientRepository
import com.herargos.herargosadmistrativo.domain.repository.ProductRepository
import com.herargos.herargosadmistrativo.domain.repository.SaleRepository
import com.herargos.herargosadmistrativo.domain.usecase.CreateSaleUseCase
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.creation_product_successful
import herargosadministrativo.composeapp.generated.resources.creation_sale_successful
import herargosadministrativo.composeapp.generated.resources.error_num_min_value_double
import herargosadministrativo.composeapp.generated.resources.not_selected_product
import herargosadministrativo.composeapp.generated.resources.update_product_successful
import herargosadministrativo.composeapp.generated.resources.update_sale_successful
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Sales screen, responsible for managing the UI state and handling business logic.
 *
 * This ViewModel interacts with various repositories to fetch and manipulate sales, client, and product data.
 * It uses a [CreateSaleUseCase] for more complex operations like creating a sale, which might involve
 * multiple steps or repositories. It communicates with the UI through a [StateFlow] of [SaleState]
 * and handles user actions via the [onEvent] method. It also uses a [Messages] utility to show
 * global messages like Snackbars to the user.
 *
 * @param clientRepository Repository for client-related data operations.
 * @param productRepository Repository for product-related data operations.
 * @param saleRepository Repository for sale-related data operations.
 * @param createSaleUseCase Use case for creating a new sale.
 * @param messages Utility for sending messages to be displayed in the UI.
 */
class SaleViewModel(
    private val clientRepository: ClientRepository,
    private val productRepository: ProductRepository,
    private val saleRepository: SaleRepository,
    private val createSaleUseCase: CreateSaleUseCase,
    private val messages: Messages
) : ViewModel() {
    private val _state: MutableStateFlow<SaleState> = MutableStateFlow(SaleState())
    /**
     * The read-only [StateFlow] of the [SaleState] which the UI observes for state changes.
     */
    val state: StateFlow<SaleState> = _state

    init {
        getClients()
        getProducts()
        getSales()
        getSaleToday()
        getSaleWeek()
        getSaleMonth()
        getSaleYear()
        getSaleEver()
    }

    /**
     * Fetches the total sales amount for all time.
     */
    private fun getSaleEver() {
        viewModelScope.launch {
            saleRepository.getTotalSalesAbsolute().collect { result ->
                when (result) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            messages.sendMessage(result.error)
                            state.copy(
                                isError = true,
                            )
                        }
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            state.copy(
                                saleEver = result.data ?: 0.0
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

    /**
     * Fetches the total sales amount for the current year.
     */
    private fun getSaleYear() {
        viewModelScope.launch {
            saleRepository.getTotalSalesThisYear().collect { result ->
                when (result) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            messages.sendMessage(result.error)
                            state.copy(
                                isError = true,
                            )
                        }
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            state.copy(
                                saleYear = result.data ?: 0.0
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

    /**
     * Fetches the total sales amount for the current month.
     */
    private fun getSaleMonth() {
        viewModelScope.launch {
            saleRepository.getTotalSalesThisMonth().collect { result ->
                when (result) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            messages.sendMessage(result.error)
                            state.copy(
                                isError = true,
                            )
                        }
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            state.copy(
                                saleMonth = result.data ?: 0.0
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

    /**
     * Fetches the total sales amount for the current week.
     */
    private fun getSaleWeek() {
        viewModelScope.launch {
            saleRepository.getTotalSalesThisWeek().collect { result ->
                when (result) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            messages.sendMessage(result.error)
                            state.copy(
                                isError = true,
                            )
                        }
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            state.copy(
                                saleWeek = result.data ?: 0.0
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

    /**
     * Fetches the total sales amount for today.
     */
    private fun getSaleToday() {
        viewModelScope.launch {
            saleRepository.getTotalSalesToday().collect { result ->
                when (result) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            messages.sendMessage(result.error)
                            state.copy(
                                isError = true,
                            )
                        }
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            state.copy(
                                saleToday = result.data ?: 0.0
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

    /**
     * Fetches a list of all sales and updates the state.
     */
    private fun getSales() {
        viewModelScope.launch {
            saleRepository.getSales().collect { result ->
                when (result) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            messages.sendMessage(result.error)
                            state.copy(
                                isError = true,
                            )
                        }
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            state.copy(
                                sales = result.data
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

    /**
     * Fetches the list of products, filtering out those already added to the current sale.
     */
    private fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect { result ->
                when (result) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            messages.sendMessage(result.error)
                            state.copy(
                                isError = true,
                            )
                        }
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            val idsInSales = state.sale.lines.map { it.idProduct }.toSet()
                            state.copy(
                                products = result.data.filter { product ->
                                    product.id !in idsInSales
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

    /**
     * Fetches the list of all clients.
     */
    private fun getClients() {
        viewModelScope.launch {
            clientRepository.getClients().collect { result ->
                when (result) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            messages.sendMessage(result.error)
                            state.copy(
                                isError = true,
                            )
                        }
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            state.copy(
                                clients = result.data
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

    /**
     * Central entry point for all user interactions from the Sales UI.
     *
     * This function receives a [SaleEvent] and delegates the handling to the corresponding
     * private function within the ViewModel.
     *
     * @param event The user action event from the UI.
     */
    fun onEvent(event: SaleEvent) {
        when (event) {
            SaleEvent.OnDisplayClient -> displayClient()
            SaleEvent.OnDismissClient -> dismissClient()
            SaleEvent.OnDismissAddClient -> dismissAddClient()
            is SaleEvent.OnDisplayAddClient -> displayAddClient(event.client)
            SaleEvent.OnDismissDeleteClient -> dismissDeleteClient()
            is SaleEvent.OnDisplayDeleteClient -> displayDeleteClient(event.client)
            SaleEvent.OnSumitClient -> sumitCLient()
            is SaleEvent.OnUpdateClient -> updateClient(event.client)
            is SaleEvent.OnDeleteClient -> deleteClient(event.client)
            is SaleEvent.OnSearchClient -> searchClient(event.text)
            SaleEvent.OnDismissSale -> dimissSale()
            is SaleEvent.OnDisplaySale -> displaySale(event.client, event.sale)
            SaleEvent.OnAddList -> addList()
            is SaleEvent.OnChangeEnablePrice -> changeEnablePrice(event.value)
            is SaleEvent.OnSearchItem -> searchItem(event.text)
            is SaleEvent.OnSelectedProduct -> selectProduct(event.product)
            SaleEvent.OnSumit -> insertSale()
            is SaleEvent.OnUpdate -> updateSale(event.sale)
            SaleEvent.OnChangeClient -> changeClient()
            SaleEvent.OnDismissSaleLines -> dimissSaleLines()
            SaleEvent.OnDisplaySaleLines -> displaySaleLines()
            is SaleEvent.OnDeleteItemList -> deleteItem(event.item)
            is SaleEvent.OnDisplaySaleDetail -> displaySaleDetail(event.sale)
            SaleEvent.OnDismissSaleDetail -> dimissSaleDetail()
            is SaleEvent.OnDeleteSale -> deleteSale(event.sale)
            SaleEvent.OnDismissDelete -> dismissDeleteSale()
            SaleEvent.OnDisplayDelete -> displayDeleteSale()
            SaleEvent.OnValidateIdentityCardClient -> validateIdentityCardClient()
            SaleEvent.OnValidateNameClient -> validateNameClient()
            SaleEvent.OnValidateQuantityProduct -> validateQuantityProduct()
            SaleEvent.OnValidatePriceSale -> validatePriceSale()
            is SaleEvent.OnIdentityCardClientChance -> identityChange(event.text)
            is SaleEvent.OnNameCLientChance -> nameChange(event.text)
            is SaleEvent.OnChanceQuantityProduct -> chanceQuantityProduct(event.text)
            is SaleEvent.OnPriceChance -> priceChance(event.text)
        }
    }

    /**
     * Updates an existing sale with the current data from the state.
     */
    private fun updateSale(sale: Sale) {
        viewModelScope.launch {
            _state.update { state ->
                val newSale = sale.copy(
                    idClient = state.client.id,
                    client = state.client,
                    createDate = dateTimeNow(),
                    lines = state.sale.lines
                )
                state.copy(sale = newSale)
            }
            when (val result = createSaleUseCase(sale)) {
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
                            displaySale = false
                        )
                    }
                    messages.sendMessage(ResponseMessage(Res.string.update_sale_successful))
                }

                else -> {}
            }
        }
    }

    /**
     * Shows the delete confirmation dialog for a sale.
     */
    private fun displayDeleteSale() {
        _state.update { state ->
            state.copy(displaySaleDelete = true)
        }
    }

    /**
     * Hides the delete confirmation dialog for a sale.
     */
    private fun dismissDeleteSale() {
        _state.update { state ->
            state.copy(displaySaleDelete = false)
        }
    }

    /**
     * Deletes a given sale.
     */
    private fun deleteSale(sale: Sale) {
        viewModelScope.launch {
            when (val result = saleRepository.deleteSale(sale)) {
                is RequestState.Error -> {
                    messages.sendMessage(result.error)
                    _state.update { state ->
                        state.copy(displaySaleDelete = false)
                    }
                }

                is RequestState.Success<*> -> {
                    _state.update { state ->
                        state.copy(
                            displaySaleDelete = false,
                            displaySaleDetail = false,
                            saleSelected = Sale()
                        )
                    }
                }

                else -> {}
            }
        }
    }

    /**
     * Hides the sale detail view.
     */
    private fun dimissSaleDetail() {
        _state.update { state ->
            state.copy(displaySaleDetail = false, sale = Sale())
        }
    }

    /**
     * Fetches and displays the details for a specific sale.
     */
    private fun displaySaleDetail(sale: Sale) {
        viewModelScope.launch {
            saleRepository.getSale(sale.id).collect { result ->
                when (result) {
                    is RequestState.Error -> {
                        _state.update { state ->
                            messages.sendMessage(result.error)
                            state.copy(displaySaleDetail = false)
                        }
                    }

                    is RequestState.Success -> {
                        _state.update { state ->
                            state.copy(displaySaleDetail = true, saleSelected = result.data)
                        }
                    }

                    else -> {}
                }
            }
        }
        getProducts()
    }

    /**
     * Creates a new sale using the data from the current state.
     */
    private fun insertSale() {
        viewModelScope.launch {
            _state.update { state ->
                val newSale = state.sale.copy(
                    idClient = state.client.id,
                    client = state.client,
                    createDate = dateTimeNow()
                )
                state.copy(sale = newSale)
            }
            when (val result = createSaleUseCase(_state.value.sale)) {
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
                            displaySale = false
                        )
                    }
                    messages.sendMessage(ResponseMessage(Res.string.creation_sale_successful))
                }

                else -> {}
            }
        }
    }

    /**
     * Removes a specific item from the current sale's line items.
     */
    private fun deleteItem(item: SaleLine) {
        _state.update { state ->
            val updatedLines = state.sale.lines.toMutableList()
            updatedLines.remove(item)
            val newSale = state.sale.copy(lines = updatedLines)
            state.copy(
                sale = newSale
            )
        }
    }

    /**
     * Hides the sale lines editor dialog.
     */
    private fun dimissSaleLines() {
        _state.update { state ->
            state.copy(
                displaySaleLine = false
            )
        }
    }

    /**
     * Shows the sale lines editor dialog.
     */
    private fun displaySaleLines() {
        _state.update { state ->
            state.copy(
                displaySaleLine = true
            )
        }
    }

    /**
     * Checks if all necessary inputs for a sale are filled and updates the state.
     */
    private fun allInputsFillSale() {
        if (_state.value.sale.lines.isNotEmpty() &&
            _state.value.sale.totalPrice.isNotEmpty()
        ) {
            _state.update { state ->
                state.copy(
                    inputsFillSale = true
                )
            }
        } else {
            _state.update { state ->
                state.copy(
                    inputsFillSale = false
                )
            }
        }
    }

    /**
     * Toggles whether the total price of a sale can be edited manually.
     */
    private fun changeEnablePrice(value: Boolean) {
        _state.update { state ->
            val price = if (value) 0.0 else state.sale.lines.sumOf { it.totalPrice }
            val newSale = state.sale.copy(totalPrice = price.toString())
            state.copy(
                enableEditPrice = value,
                sale = newSale,
                errorPrice = if (value) Res.string.error_num_min_value_double else null
            )
        }
    }

    /**
     * Adds the currently selected product to the sale's line items.
     */
    private fun addList() {
        val newList = _state.value.sale.lines.toMutableList()
        newList.add(
            SaleLine(
                idProduct = _state.value.product.id,
                productName = _state.value.product.name,
                productStock = _state.value.productQuantity.toDouble(),
                singlePrice = _state.value.product.price.toDouble(),
                totalPrice = _state.value.productQuantity.toDouble() * _state.value.product.price.toDouble()
            )
        )
        _state.update { state ->
            val price = if (state.enableEditPrice) 0.0 else newList.sumOf { it.totalPrice }
            val newSale = state.sale.copy(totalPrice = price.toString(), lines = newList)
            state.copy(
                productQuantity = "",
                productPrice = "",
                searchProduct = "",
                sale = newSale
            )
        }
        getProducts()
        allInputsFillSale()
    }

    /**
     * Updates the search text for products.
     */
    private fun searchItem(text: String) {
        _state.update { state ->
            state.copy(searchProduct = text)
        }
    }

    /**
     * Sets the currently selected product in the state.
     */
    private fun selectProduct(product: Product) {
        _state.update { state ->
            state.copy(product = product)
        }
    }

    /**
     * Navigates back to the client selection view from the sale creation view.
     */
    private fun changeClient() {
        _state.update { state ->
            state.copy(displayClient = true, displaySale = false)
        }
    }

    /**
     * Displays the sale creation/editing view for a given client and sale.
     */
    private fun displaySale(client: Client, sale: Sale) {
        _state.update { state ->
            state.copy(
                displayClient = false,
                displaySale = true,
                client = client,
                sale = sale
            )
        }
    }

    /**
     * Hides the sale creation/editing view.
     */
    private fun dimissSale() {
        _state.update { state ->
            state.copy(displaySale = false)
        }
    }

    /**
     * Updates the search text for clients.
     */
    private fun searchClient(text: String) {
        _state.update { state ->
            state.copy(
                searchClient = text
            )
        }
    }

    /**
     * Shows the delete confirmation dialog for a specific client.
     */
    private fun displayDeleteClient(client: Client) {
        _state.update { state ->
            state.copy(displayDeleteClient = true, client = client)
        }
    }

    /**
     * Hides the client delete confirmation dialog.
     */
    private fun dismissDeleteClient() {
        _state.update { state ->
            state.copy(displayDeleteClient = false, client = Client())
        }
    }

    /**
     * Deletes a given client.
     */
    private fun deleteClient(client: Client) {
        viewModelScope.launch {
            when (val result = clientRepository.deleteClient(client = client)) {
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
                    _state.update { state ->
                        state.copy(
                            isSuccess = false,
                            displayDeleteClient = false
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

    /**
     * Updates a given client's information.
     */
    private fun updateClient(client: Client) {
        viewModelScope.launch {
            when (val result = clientRepository.updateClient(client = client)) {
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
                            displayAddClient = false
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

    /**
     * Submits the current client data to create a new client.
     */
    private fun sumitCLient() {
        viewModelScope.launch {
            when (val result = clientRepository.createClient(client = _state.value.client)) {
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
                            displayAddClient = false
                        )
                    }
                    messages.sendMessage(ResponseMessage(Res.string.creation_product_successful))
                }

                else -> {
                    _state.update { state ->
                        state.copy(isLoading = true)
                    }
                }
            }
        }
    }

    /**
     * Hides the add/edit client dialog.
     */
    private fun dismissAddClient() {
        _state.update { state ->
            state.copy(displayAddClient = false)
        }
    }

    /**
     * Displays the add/edit client dialog, pre-filling it with the given client's data.
     */
    private fun displayAddClient(client: Client) {
        _state.update { state ->
            state.copy(displayAddClient = true, client = client)
        }
    }

    /**
     * Hides the client selection list.
     */
    private fun dismissClient() {
        _state.update { state ->
            state.copy(displayClient = false)
        }
    }

    /**
     * Shows the client selection list.
     */
    private fun displayClient() {
        _state.update { state ->
            state.copy(displayClient = true)
        }
    }

    /**
     * Checks if all required input fields for a client are filled.
     */
    private fun allInputsFill() {
        if (_state.value.client.name.isNotEmpty() &&
            _state.value.client.identityCard.isNotEmpty()
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


    /**
     * Validates the client's identity card number.
     */
    private fun validateIdentityCardClient() {
        _state.update { state ->
            val error =
                state.client.identityCard.isErrorInt(minValue = 1000000, maxValue = 100000000)
            state.copy(errorIdentityCard = error)
        }
        updateInputError()
    }

    /**
     * Validates the client's name.
     */
    private fun validateNameClient() {
        _state.update { state ->
            state.copy(errorName = state.client.name.isError())
        }
        updateInputError()
    }

    /**
     * Validates the product quantity input field.
     */
    private fun validateQuantityProduct() {
        _state.update { state ->
            val error = if (state.product.id == 0) {
                Res.string.not_selected_product
            } else {
                state.productQuantity.isErrorDouble()
            }
            state.copy(errorQuantity = error)
        }
        updateInputError()
    }

    /**
     * Validates the sale's total price input field.
     */
    private fun validatePriceSale() {
        _state.update { state ->
            val error = if (state.enableEditPrice) state.sale.totalPrice.isErrorDouble() else null
            state.copy(errorPrice = error)
        }
        updateInputError()
    }

    /**
     * Aggregates all individual validation errors into a single state flag.
     */
    private fun updateInputError() {
        _state.update { state ->
            val allErrors = (state.errorIdentityCard != null) ||
                    (state.errorName != null) ||
                    (state.errorQuantity != null) ||
                    (state.errorPrice != null)
            state.copy(inputError = allErrors)
        }
    }

    /**
     * Updates the client's name in the state and triggers validation.
     */
    private fun nameChange(text: String) {
        _state.update { it.copy(client = it.client.copy(name = text)) }
        validateNameClient()
        allInputsFill()
    }

    /**
     * Updates the client's identity card in the state and triggers validation.
     */
    private fun identityChange(text: String) {
        _state.update { it.copy(client = it.client.copy(identityCard = text)) }
        validateIdentityCardClient()
        allInputsFill()
    }

    /**
     * Updates the product quantity in the state and triggers validation.
     */
    private fun chanceQuantityProduct(text: String) {
        _state.update { state ->
            val price = try {
                if (text.isNotEmpty()) {
                    (state.product.price.toDouble() * text.toDouble()).toString()
                } else {
                    ""
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                ""
            }
            state.copy(
                productQuantity = text,
                productPrice = price
            )
        }
        validateQuantityProduct()
    }

    /**
     * Updates the sale's total price in the state and triggers validation.
     */
    private fun priceChance(text: String) {
        _state.update { state ->
            state.copy(sale = state.sale.copy(totalPrice = text))
        }
        validatePriceSale()
        allInputsFillSale()
    }
}