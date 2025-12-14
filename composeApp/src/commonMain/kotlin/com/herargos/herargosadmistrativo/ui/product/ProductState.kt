package com.herargos.herargosadmistrativo.ui.product

import com.herargos.herargosadmistrativo.domain.model.Ingredient
import com.herargos.herargosadmistrativo.domain.model.Product
import com.herargos.herargosadmistrativo.domain.model.Recipe
import org.jetbrains.compose.resources.StringResource

data class ProductState(
    val products: List<Product> = listOf(),
    val productSelected: Product = Product(),
    val product: Product = Product(),

    val imageData: ByteArray? = null,

    val ingredientsDB: List<Ingredient> = listOf(),

    val searchText: String = "",

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: Boolean = false,
    val messageError: StringResource? = null,

    val showDialog: Boolean = false,
    val showDialogDelete: Boolean = false,
    val showDialogList: Boolean = false,
    val showDialogDetail: Boolean = false,

    val showSnackBar: Boolean = false,
    val textSnackBar: StringResource? = null,

    val enablePrice: Boolean = false,
    val enableQuantity: Boolean = false,

    val ingredientSelected: Ingredient = Ingredient(),
    val recipe: List<Recipe> = listOf(),
    val recipeAux: List<Recipe> = listOf(),

    val ingredientQuantity: String = "",
    val ingredientPrice: String = "",

    val inputsFill: Boolean = false,
    val inputError: Boolean = false,
    val errorName: StringResource? = null,
    val errorStock: StringResource? = null,
    val errorQuantity: StringResource? = null, // Nuevo campo para la validación de cantidad
    val errorIngredient: StringResource? = null,
    val errorPrice: StringResource? = null,

    val originalRecipeQuantity: Double = 0.0,
)