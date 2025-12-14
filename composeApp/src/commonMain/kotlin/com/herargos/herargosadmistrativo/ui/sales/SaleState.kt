package com.herargos.herargosadmistrativo.ui.sales

import com.herargos.herargosadmistrativo.domain.model.Client
import com.herargos.herargosadmistrativo.domain.model.Product
import com.herargos.herargosadmistrativo.domain.model.Sale
import org.jetbrains.compose.resources.StringResource

data class SaleState(
    val displayClient: Boolean = false,
    val displayAddClient: Boolean = false,
    val displayDeleteClient: Boolean = false,
    val displaySale: Boolean = false,
    val displaySaleLine: Boolean = false,
    val displaySaleDetail: Boolean = false,
    val displaySaleDelete: Boolean = false,

    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,

    val inputError: Boolean = false,
    val inputsFill: Boolean = false,
    val inputErrorSale: Boolean = false,
    val inputsFillSale: Boolean = false,

    val errorIdentityCard: StringResource? = null,
    val errorName: StringResource? = null,

    val client: Client = Client(),
    val clients: List<Client> = listOf(),
    val searchClient: String = "",

    val products: List<Product> = listOf(),
    val product: Product = Product(),
    val searchProduct: String = "",
    val productQuantity: String = "",
    val errorQuantity: StringResource? = null,
    val productPrice: String = "",
    val productSelected: Int = 0,

    val enableEditPrice: Boolean = false,
    val errorPrice: StringResource? = null,

    val sale: Sale = Sale(),
    val saleSelected: Sale = Sale(),
    val sales: List<Sale> = listOf(),

    val saleToday: Double = 0.0,
    val saleWeek: Double = 0.0,
    val saleMonth: Double = 0.0,
    val saleYear: Double = 0.0,
    val saleEver: Double = 0.0,
)
