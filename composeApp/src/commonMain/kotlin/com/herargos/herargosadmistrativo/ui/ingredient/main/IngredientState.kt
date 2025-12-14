package com.herargos.herargosadmistrativo.ui.ingredient.main

import com.herargos.herargosadmistrativo.domain.model.Ingredient
import org.jetbrains.compose.resources.StringResource

data class IngredientState(
    val ingredients: List<Ingredient> = listOf(),
    val imageData: ByteArray? = null,

    val ingredientSelected: Ingredient = Ingredient(),

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: Boolean = false,
    val messageError: StringResource? = null,

    val showDialog: Boolean = false,
    val showDialogDelete: Boolean = false,

    val ingredient: Ingredient = Ingredient(),

    val inputsFill: Boolean = false,
    val inputError: Boolean = false,
    val errorName: StringResource? = null,
    val errorStock: StringResource? = null,
    val errorUnit: StringResource? = null,
    val errorPrice: StringResource? = null,
)