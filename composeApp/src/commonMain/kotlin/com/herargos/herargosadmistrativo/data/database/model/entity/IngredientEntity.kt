package com.herargos.herargosadmistrativo.data.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.herargos.herargosadmistrativo.domain.model.Ingredient

@Entity
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val stock: Double = 0.0,
    val unit: String = "",
    val price: Double = 0.0,
    val imagePath: String? = null,
    val isAlive: Boolean = true
) {
    fun toDomain(): Ingredient {
        return Ingredient(
            id = id,
            name = name,
            stock = stock.toString(),
            unit = unit,
            price = price.toString(),
            imagePath = imagePath
        )
    }
}
