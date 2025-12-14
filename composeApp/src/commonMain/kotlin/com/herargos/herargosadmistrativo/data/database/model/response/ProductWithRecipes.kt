package com.herargos.herargosadmistrativo.data.database.model.response

import androidx.room.Embedded
import androidx.room.Relation
import com.herargos.herargosadmistrativo.data.database.model.entity.ProductEntity
import com.herargos.herargosadmistrativo.data.database.model.entity.RecipeEntity
import com.herargos.herargosadmistrativo.domain.model.Product

data class ProductWithRecipes(
    @Embedded
    val product: ProductEntity,
    @Relation(
        parentColumn = "id", // Columna en ProductEntity
        entityColumn = "idProduct" // Columna en RecipeEntity que referencia a ProductEntity
    )
    val recipes: List<RecipeEntity>
) {
    fun toDomain(): Product {
        return Product(
            id = product.id,
            name = product.name,
            stock = product.stock.toString(),
            price = product.price.toString(),
            imagePath = product.imagePath,
            recipes = recipes.map { it.toDomain() }
        )
    }
}