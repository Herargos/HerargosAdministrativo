package com.herargos.herargosadmistrativo.ui.ingredient.main

import com.herargos.herargosadmistrativo.domain.model.Ingredient

sealed interface IngredientEvents {

    data object OnBack : IngredientEvents
    data object OnDismiss : IngredientEvents
    data class OnDisplay(val ingredient: Ingredient?) : IngredientEvents

    data object OnDismissDelete : IngredientEvents
    data class OnDisplayDelete(val ingredient: Ingredient) : IngredientEvents
    data class OnConfirmDelete(val ingredient: Ingredient) : IngredientEvents

    data class OnNameChanged(val text: String) : IngredientEvents
    data object OnValidateName : IngredientEvents // Nuevo
    data class OnStockChanged(val text: String) : IngredientEvents
    data object OnValidateStock : IngredientEvents // Nuevo
    data class OnUnitChanged(val text: String) : IngredientEvents
    data object OnValidateUnit : IngredientEvents // Nuevo
    data class OnPriceChanged(val text: String) : IngredientEvents
    data object OnValidatePrice : IngredientEvents // Nuevo
    data class OnImageChanged(val imageData: ByteArray) : IngredientEvents
    data object OnSubmitIngredient : IngredientEvents

    data class OnUpdateIngredient(val ingredient: Ingredient) : IngredientEvents
}