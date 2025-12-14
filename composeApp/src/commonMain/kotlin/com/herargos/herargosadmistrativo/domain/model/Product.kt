package com.herargos.herargosadmistrativo.domain.model

import com.herargos.herargosadmistrativo.data.database.model.entity.ProductEntity
import com.herargos.herargosadmistrativo.data.database.model.response.ProductWithRecipes

data class Product(
    val id: Int = 0,
    val name: String = "",
    val stock: String = "",
    val price: String = "",
    val imagePath: String? = null,
    val recipes: List<Recipe> = listOf()
) {
    fun toEntity(): ProductEntity {
        return ProductEntity(
            id = id,
            name = name,
            stock = stock.toDouble(),
            price = price.toDouble(),
            imagePath = imagePath
        )
    }

    fun toProductWithRecipes(): ProductWithRecipes {
        return ProductWithRecipes(
            product = this.toEntity(),
            recipes = recipes.map { it.toEntity() }
        )
    }
}