package com.herargos.herargosadmistrativo.domain.model

import com.herargos.herargosadmistrativo.data.database.model.entity.RecipeEntity

data class Recipe(
    val idIngredient: Int = 0,
    val idProduct: Int = 0,
    val name: String = "",
    val quantity: Double = 0.0,
    val price: Double = 0.0
) {
    fun toEntity():RecipeEntity {
        return RecipeEntity(
            idIngredient = idIngredient,
            name = name,
            quantity = quantity,
            price = price
        )
    }
}
