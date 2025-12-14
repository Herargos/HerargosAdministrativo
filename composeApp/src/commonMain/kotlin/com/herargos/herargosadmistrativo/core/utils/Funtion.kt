package com.herargos.herargosadmistrativo.core.utils

import com.herargos.herargosadmistrativo.domain.model.Ingredient
import com.herargos.herargosadmistrativo.ui.product.model.IngredientUI

fun<T> getUniqueItemsFromFirstList(
    list1: List<T>,
    list2: List<T>
): List<T> {
    // Convierte la segunda lista a un Set para una búsqueda eficiente.
    // Usamos 'toSet()' que por defecto usa 'equals()' y 'hashCode()'
    // de la data class para determinar la igualdad de los elementos.
    val set2 = list2.toSet()

    // Filtra la primera lista, manteniendo solo los elementos que NO están en set2.
    return list1.filter { it !in set2 }
}

fun getUniqueIngredients(
    ingredients: List<Ingredient>,
    ingredientUIs: List<IngredientUI>
): List<Ingredient> {
    // Extract all ingredient IDs from the IngredientUI list for efficient lookup
    val uiIngredientIds = ingredientUIs.map { it.idIngredient }.toSet()

    // Filter the original ingredients list
    return ingredients.filter { ingredient ->
        // Return true if the ingredient's ID is NOT present in the uiIngredientIds set
        ingredient.id !in uiIngredientIds
    }
}