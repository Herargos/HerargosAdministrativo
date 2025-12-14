package com.herargos.herargosadmistrativo.ui.sales

import com.herargos.herargosadmistrativo.domain.model.Client
import com.herargos.herargosadmistrativo.domain.model.Product
import com.herargos.herargosadmistrativo.domain.model.Sale
import com.herargos.herargosadmistrativo.domain.model.SaleLine

sealed interface SaleEvent {
    data object OnDisplayClient : SaleEvent
    data object OnDismissClient : SaleEvent

    data class OnDisplayAddClient(val client: Client = Client()) : SaleEvent
    data object OnDismissAddClient : SaleEvent

    data class OnDisplayDeleteClient(val client: Client = Client()) : SaleEvent
    data object OnDismissDeleteClient : SaleEvent

    data class OnNameCLientChance(val text: String) : SaleEvent
    data class OnIdentityCardClientChance(val text: String) : SaleEvent

    data object OnSumitClient : SaleEvent
    data class OnUpdateClient(val client: Client) : SaleEvent
    data class OnDeleteClient(val client: Client) : SaleEvent
    data class OnSearchClient(val text: String) : SaleEvent
    data object OnChangeClient : SaleEvent

    data class OnDisplaySale(val client: Client, val sale: Sale = Sale()) : SaleEvent
    data object OnDismissSale : SaleEvent

    data class OnSelectedProduct(val product: Product) : SaleEvent
    data class OnSearchItem(val text: String) : SaleEvent
    data class OnChanceQuantityProduct(val text: String) : SaleEvent

    data object OnAddList : SaleEvent
    data class OnDeleteItemList(val item: SaleLine) : SaleEvent

    data class OnPriceChance(val text: String) : SaleEvent
    data class OnChangeEnablePrice(val value: Boolean) : SaleEvent

    data object OnSumit : SaleEvent
    data class OnUpdate(val sale: Sale) : SaleEvent
    data class OnDeleteSale(val sale: Sale) : SaleEvent

    data object OnDisplaySaleLines : SaleEvent
    data object OnDismissSaleLines : SaleEvent

    data class OnDisplaySaleDetail(val sale: Sale) : SaleEvent
    data object OnDismissSaleDetail : SaleEvent

    data object OnDisplayDelete : SaleEvent
    data object OnDismissDelete : SaleEvent

    data object OnValidateIdentityCardClient : SaleEvent
    data object OnValidateNameClient : SaleEvent
    data object OnValidateQuantityProduct : SaleEvent
    data object OnValidatePriceSale : SaleEvent
}