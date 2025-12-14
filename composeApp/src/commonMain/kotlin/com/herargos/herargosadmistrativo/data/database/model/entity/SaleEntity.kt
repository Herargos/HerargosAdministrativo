package com.herargos.herargosadmistrativo.data.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.herargos.herargosadmistrativo.core.utils.dateTimeNow

@Entity
data class SaleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idClient: Int = 0,
    val totalPrice: Double = 0.0,
    val createDate: String = dateTimeNow(),
    val isAlive: Boolean = true
)
