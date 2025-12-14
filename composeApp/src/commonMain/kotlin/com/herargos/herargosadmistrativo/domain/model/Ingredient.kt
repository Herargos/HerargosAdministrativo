package com.herargos.herargosadmistrativo.domain.model

import com.herargos.herargosadmistrativo.data.database.model.entity.IngredientEntity

data class Ingredient(
    val id: Int = 0,
    val name: String = "",
    val stock: String = "",
    val unit: String = "",
    val price: String = "",
    val imagePath: String? = null,
) {
    fun toEntity(): IngredientEntity {
        return IngredientEntity(
            id = id,
            name = name,
            stock = stock.toDouble(),
            unit = unit,
            price = price.toDouble(),
            imagePath = imagePath
        )
    }
}
