package com.herargos.herargosadmistrativo.data.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Establece un valor predeterminado para la clave primaria autogenerada
    val name: String = "",
    val stock: Double = 0.0,
    val price: Double = 0.0,
    val imagePath: String? = null,
    val isAlive: Boolean = true
)
