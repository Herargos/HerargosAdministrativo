package com.herargos.herargosadmistrativo.ui.product

import com.herargos.herargosadmistrativo.domain.model.Ingredient
import com.herargos.herargosadmistrativo.domain.model.Product
import com.herargos.herargosadmistrativo.domain.model.Recipe
import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientEvents

sealed interface ProductEvents {
    data object OnBack : ProductEvents
    data object OnDismiss : ProductEvents
    data class OnDisplay(val product: Product?) : ProductEvents

    data object OnDismissDelete : ProductEvents
    data class OnDisplayDelete(val product: Product) : ProductEvents
    data class OnConfirmDelete(val product: Product) : ProductEvents

    // Eventos para actualizar el valor del campo
    data class OnNameChance(val text: String) : ProductEvents
    data class OnStockChance(val text: String) : ProductEvents
    data class OnPriceChance(val text: String) : ProductEvents

    // Nuevos eventos de validación al perder el foco
    data object OnValidateName : ProductEvents
    data object OnValidateStock : ProductEvents
    data object OnValidatePrice : ProductEvents
    data object OnValidateQuantity : ProductEvents
    data class OnImageChanged(val imageData: ByteArray) : ProductEvents

    data object OnSubmit : ProductEvents

    data object OnUpdate : ProductEvents

    data class OnSearchItem(val text: String) : ProductEvents

    data class OnChangeEnablePrice(val check: Boolean) : ProductEvents

    data object OnDismissList : ProductEvents
    data object OnDisplayList : ProductEvents
    data object OnAddList : ProductEvents
    data class OnDeleteItemList(val recipe: Recipe) : ProductEvents

    data class OnSelectedIngredient(val ingredient: Ingredient) : ProductEvents
    data class OnChanceQuantityIngredient(val quantity: String) : ProductEvents

    data object OnDismissDetail : ProductEvents
    data class OnDisplayDetail(val product: Product) : ProductEvents
}