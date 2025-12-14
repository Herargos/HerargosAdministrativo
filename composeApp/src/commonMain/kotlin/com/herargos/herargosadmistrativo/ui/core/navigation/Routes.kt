package com.herargos.herargosadmistrativo.ui.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    object Home

    @Serializable
    object Ingredient

    @Serializable
    data class IngredientForm(val id: Int? = null)

    @Serializable
    object Recipe

    @Serializable
    object Product

    @Serializable
    object Sale
}
