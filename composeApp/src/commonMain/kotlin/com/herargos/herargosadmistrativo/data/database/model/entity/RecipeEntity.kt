package com.herargos.herargosadmistrativo.data.database.model.entity

import androidx.room.Entity
import com.herargos.herargosadmistrativo.domain.model.Recipe

@Entity(primaryKeys = ["idIngredient", "idProduct"]) // Clave primaria compuesta para la tabla de unión
data class RecipeEntity(
    val idProduct: Int = 0,
    val idIngredient: Int = 0,
    val name: String = "",
    val quantity: Double = 0.0,
    val price: Double = 0.0
) {
    fun toDomain(): Recipe {
        return Recipe(
            idIngredient = idIngredient,
            idProduct = idProduct,
            name = name,
            quantity = quantity,
            price = price
        )
    }
}
