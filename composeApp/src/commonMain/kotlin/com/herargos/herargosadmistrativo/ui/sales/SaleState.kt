package com.herargos.herargosadmistrativo.ui.sales

import com.herargos.herargosadmistrativo.domain.model.Client
import com.herargos.herargosadmistrativo.domain.model.Product
import com.herargos.herargosadmistrativo.domain.model.Sale
import org.jetbrains.compose.resources.StringResource

/**
 * Represents the entire state for the Sales screen.
 *
 * This data class holds all the information the UI needs to render itself, including
 * visibility flags for dialogs, loading and error states, form inputs, validation errors,
 * and the actual data like clients, products, and sales.
 */
data class SaleState(
    // region UI Visibility Flags
    /** Controls the visibility of the client selection dialog. */
    val displayClient: Boolean = false,
    /** Controls the visibility of the add/edit client dialog. */
    val displayAddClient: Boolean = false,
    /** Controls the visibility of the delete client confirmation dialog. */
    val displayDeleteClient: Boolean = false,
    /** Controls the visibility of the main sale creation/editing view. */
    val displaySale: Boolean = false,
    /** Controls the visibility of the sale line items dialog. */
    val displaySaleLine: Boolean = false,
    /** Controls the visibility of the sale details view. */
    val displaySaleDetail: Boolean = false,
    /** Controls the visibility of the delete sale confirmation dialog. */
    val displaySaleDelete: Boolean = false,
    // endregion

    // region Async Operation State
    /** True when a background operation (e.g., network or database call) is in progress. */
    val isLoading: Boolean = false,
    /** True if a recent async operation resulted in an error. */
    val isError: Boolean = false,
    /** True if a recent async operation completed successfully. */
    val isSuccess: Boolean = false,
    // endregion

    // region Form & Input State
    /** General flag indicating if there is any validation error in the client form. */
    val inputError: Boolean = false,
    /** True when all required fields in the client form are filled. */
    val inputsFill: Boolean = false,
    /** General flag indicating if there is any validation error in the sale form. */
    val inputErrorSale: Boolean = false,
    /** True when all required fields in the sale form are filled. */
    val inputsFillSale: Boolean = false,
    // endregion

    // region Client Data & Form Fields
    /** Holds the specific error message for the client's identity card field. */
    val errorIdentityCard: StringResource? = null,
    /** Holds the specific error message for the client's name field. */
    val errorName: StringResource? = null,
    /** The client currently being created, edited, or selected for a sale. */
    val client: Client = Client(),
    /** The list of all available clients. */
    val clients: List<Client> = listOf(),
    /** The current text in the client search input field. */
    val searchClient: String = "",
    // endregion

    // region Product Data & Form Fields
    /** The list of all available products. */
    val products: List<Product> = listOf(),
    /** The product currently selected to be added to a sale. */
    val product: Product = Product(),
    /** The current text in the product search input field. */
    val searchProduct: String = "",
    /** The value of the quantity input field for a product. */
    val productQuantity: String = "",
    /** Holds the specific error message for the product quantity field. */
    val errorQuantity: StringResource? = null,
    /** The calculated price for the given quantity of a product. */
    val productPrice: String = "",
    /** The ID of the selected product. */
    val productSelected: Int = 0,
    // endregion

    // region Sale Data & Form Fields
    /** When true, allows manual editing of the sale's total price. */
    val enableEditPrice: Boolean = false,
    /** Holds the specific error message for the sale's total price field. */
    val errorPrice: StringResource? = null,
    /** The sale object currently being created or edited. */
    val sale: Sale = Sale(),
    /** The sale currently selected to view its details. */
    val saleSelected: Sale = Sale(),
    /** The list of all sales. */
    val sales: List<Sale> = listOf(),
    // endregion

    // region Sales Statistics
    /** Total sales amount for the current day. */
    val saleToday: Double = 0.0,
    /** Total sales amount for the current week. */
    val saleWeek: Double = 0.0,
    /** Total sales amount for the current month. */
    val saleMonth: Double = 0.0,
    /** Total sales amount for the current year. */
    val saleYear: Double = 0.0,
    /** Total sales amount for all time. */
    val saleEver: Double = 0.0,
)
