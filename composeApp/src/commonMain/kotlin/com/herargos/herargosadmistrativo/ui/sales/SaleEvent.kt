package com.herargos.herargosadmistrativo.ui.sales

import com.herargos.herargosadmistrativo.domain.model.Client
import com.herargos.herargosadmistrativo.domain.model.Product
import com.herargos.herargosadmistrativo.domain.model.Sale
import com.herargos.herargosadmistrativo.domain.model.SaleLine

/**
 * Defines all possible user actions and events that can occur on the Sales screen.
 *
 * This sealed interface is used to pass events from the UI (Composables) to the [SaleViewModel]
 * for processing. Each object or data class represents a specific user interaction.
 */
sealed interface SaleEvent {
    // region Client Management Events
    /** Event to show the client selection dialog. */
    data object OnDisplayClient : SaleEvent
    /** Event to hide the client selection dialog. */
    data object OnDismissClient : SaleEvent

    /** Event to show the dialog for adding or editing a client. */
    data class OnDisplayAddClient(val client: Client = Client()) : SaleEvent
    /** Event to hide the add/edit client dialog. */
    data object OnDismissAddClient : SaleEvent

    /** Event to show the confirmation dialog for deleting a client. */
    data class OnDisplayDeleteClient(val client: Client = Client()) : SaleEvent
    /** Event to hide the delete client confirmation dialog. */
    data object OnDismissDeleteClient : SaleEvent

    /** Event triggered when the client's name input changes. */
    data class OnNameCLientChance(val text: String) : SaleEvent
    /** Event triggered when the client's identity card input changes. */
    data class OnIdentityCardClientChance(val text: String) : SaleEvent

    /** Event to submit (create) a new client. */
    data object OnSumitClient : SaleEvent
    /** Event to update an existing client. */
    data class OnUpdateClient(val client: Client) : SaleEvent
    /** Event to delete a client. */
    data class OnDeleteClient(val client: Client) : SaleEvent
    /** Event triggered when the search text for clients changes. */
    data class OnSearchClient(val text: String) : SaleEvent
    /** Event to trigger changing the client for a sale, usually returning to the client list. */
    data object OnChangeClient : SaleEvent
    // endregion

    // region Sale Creation/Editing Events
    /** Event to show the main sale creation/editing form. */
    data class OnDisplaySale(val client: Client, val sale: Sale = Sale()) : SaleEvent
    /** Event to hide the sale creation/editing form. */
    data object OnDismissSale : SaleEvent

    /** Event triggered when a product is selected from the autocomplete list. */
    data class OnSelectedProduct(val product: Product) : SaleEvent
    /** Event triggered when the search text for products changes. */
    data class OnSearchItem(val text: String) : SaleEvent
    /** Event triggered when the product quantity input changes. */
    data class OnChanceQuantityProduct(val text: String) : SaleEvent

    /** Event to add the selected product as a line item to the current sale. */
    data object OnAddList : SaleEvent
    /** Event to remove a specific line item from the current sale. */
    data class OnDeleteItemList(val item: SaleLine) : SaleEvent

    /** Event triggered when the sale's total price input changes. */
    data class OnPriceChance(val text: String) : SaleEvent
    /** Event to toggle whether the total price can be manually edited. */
    data class OnChangeEnablePrice(val value: Boolean) : SaleEvent

    /** Event to submit (create) the current sale. */
    data object OnSumit : SaleEvent
    /** Event to update the current sale. */
    data class OnUpdate(val sale: Sale) : SaleEvent
    /** Event to delete a sale. */
    data class OnDeleteSale(val sale: Sale) : SaleEvent
    // endregion

    // region Sale Details Events
    /** Event to show the dialog displaying the list of sale line items. */
    data object OnDisplaySaleLines : SaleEvent
    /** Event to hide the sale line items dialog. */
    data object OnDismissSaleLines : SaleEvent

    /** Event to show the details of a specific past sale. */
    data class OnDisplaySaleDetail(val sale: Sale) : SaleEvent
    /** Event to hide the sale detail view. */
    data object OnDismissSaleDetail : SaleEvent
    // endregion

    // region General Dialog Events
    /** Event to show the generic delete confirmation dialog (for a sale). */
    data object OnDisplayDelete : SaleEvent
    /** Event to hide the generic delete confirmation dialog. */
    data object OnDismissDelete : SaleEvent
    // endregion

    // region Validation Events
    /** Event to trigger validation for the client's identity card field. */
    data object OnValidateIdentityCardClient : SaleEvent
    /** Event to trigger validation for the client's name field. */
    data object OnValidateNameClient : SaleEvent
    /** Event to trigger validation for the product quantity field. */
    data object OnValidateQuantityProduct : SaleEvent
    /** Event to trigger validation for the sale's total price field. */
    data object OnValidatePriceSale : SaleEvent
    // endregion
}