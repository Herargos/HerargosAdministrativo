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


class SaleViewModel(
    private val clientRepository: ClientRepository,
    private val productRepository: ProductRepository,
    private val saleRepository: SaleRepository,
    private val createSaleUseCase: CreateSaleUseCase,
    private val messages: Messages
) : ViewModel() {
    private val _state: MutableStateFlow<SaleState> = MutableStateFlow(SaleState())
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
            when (val result = createSaleUseCase(sale)/*saleRepository.updateSale(_state.value.sale)*/) {
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

    private fun displayDeleteSale() {
        _state.update { state ->
            state.copy(displaySaleDelete = true)
        }
    }

    private fun dismissDeleteSale() {
        _state.update { state ->
            state.copy(displaySaleDelete = false)
        }
    }

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

    private fun dimissSaleDetail() {
        _state.update { state ->
            state.copy(displaySaleDetail = false, sale = Sale())
        }
    }

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
            when (val result = createSaleUseCase(_state.value.sale) /*saleRepository.createSale(_state.value.sale)*/) {
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

    private fun dimissSaleLines() {
        _state.update { state ->
            state.copy(
                displaySaleLine = false
            )
        }
    }

    private fun displaySaleLines() {
        _state.update { state ->
            state.copy(
                displaySaleLine = true
            )
        }
    }

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

    private fun changeEnablePrice(value: Boolean) {
        _state.update { state ->
            //state.copy(enableEditPrice = value)
            val price = if (value) 0.0 else state.sale.lines.sumOf { it.totalPrice }
            val newSale = state.sale.copy(totalPrice = price.toString())
            state.copy(
                enableEditPrice = value,
                sale = newSale,
                errorPrice = if (value) Res.string.error_num_min_value_double else null
            )
        }
    }

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

    private fun searchItem(text: String) {
        _state.update { state ->
            state.copy(searchProduct = text)
        }
    }

    private fun selectProduct(product: Product) {
        _state.update { state ->
            state.copy(product = product)
        }
    }

    private fun changeClient() {
        _state.update { state ->
            state.copy(displayClient = true, displaySale = false)
        }
    }

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

    private fun dimissSale() {
        _state.update { state ->
            state.copy(displaySale = false)
        }
    }

    private fun searchClient(text: String) {
        _state.update { state ->
            state.copy(
                searchClient = text
            )
        }
    }

    private fun displayDeleteClient(client: Client) {
        _state.update { state ->
            state.copy(displayDeleteClient = true, client = client)
        }
    }

    private fun dismissDeleteClient() {
        _state.update { state ->
            state.copy(displayDeleteClient = false, client = Client())
        }
    }

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

    private fun dismissAddClient() {
        _state.update { state ->
            state.copy(displayAddClient = false)
        }
    }

    private fun displayAddClient(client: Client) {
        _state.update { state ->
            state.copy(displayAddClient = true, client = client)
        }
    }

    private fun dismissClient() {
        _state.update { state ->
            state.copy(displayClient = false)
        }
    }

    private fun displayClient() {
        _state.update { state ->
            state.copy(displayClient = true)
        }
    }

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

    // Métodos de validación individuales
    private fun validateIdentityCardClient() {
        _state.update { state ->
            val error =
                state.client.identityCard.isErrorInt(minValue = 1000000, maxValue = 100000000)
            state.copy(errorIdentityCard = error)
        }
        updateInputError()
    }

    private fun validateNameClient() {
        _state.update { state ->
            state.copy(errorName = state.client.name.isError())
        }
        updateInputError()
    }

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

    private fun validatePriceSale() {
        _state.update { state ->
            val error = if (state.enableEditPrice) state.sale.totalPrice.isErrorDouble() else null
            state.copy(errorPrice = error)
        }
        updateInputError()
    }

    // Método para actualizar el estado de error general
    private fun updateInputError() {
        _state.update { state ->
            val allErrors = (state.errorIdentityCard != null) ||
                    (state.errorName != null) ||
                    (state.errorQuantity != null) ||
                    (state.errorPrice != null)
            state.copy(inputError = allErrors)
        }
    }

    private fun nameChange(text: String) {
        _state.update { it.copy(client = it.client.copy(name = text)) }
        validateNameClient()
        allInputsFill()
    }

    private fun identityChange(text: String) {
        _state.update { it.copy(client = it.client.copy(identityCard = text)) }
        validateIdentityCardClient()
        allInputsFill()
    }

    private fun chanceQuantityProduct(text: String) {
        _state.update { state ->
            val price = try {
                if (text.isNotEmpty()) {
                    (state.product.price.toDouble() * text.toDouble()).toString()
                } else {
                    ""
                }
            } catch (e: NumberFormatException) {
                ""
            }
            state.copy(
                productQuantity = text,
                productPrice = price
            )
        }
        validateQuantityProduct()
    }

    private fun priceChance(text: String) {
        _state.update { state ->
            state.copy(sale = state.sale.copy(totalPrice = text))
        }
        validatePriceSale()
        allInputsFillSale()
    }
}