package com.herargos.herargosadmistrativo.data.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.herargos.herargosadmistrativo.domain.model.SaleLine

@Entity
data class SaleLineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idSale: Int = 0,
    val idProduct: Int = 0,
    val productName: String = "",
    val productStock: Double = 0.0,
    val singlePrice: Double = 0.0,
    val totalPrice: Double = 0.0
) {
    fun toDomain(): SaleLine {
        return SaleLine(
            id = id,
            idSale = idSale,
            idProduct = idProduct,
            productName = productName,
            productStock = productStock,
            singlePrice = singlePrice,
            totalPrice = totalPrice
        )
    }
}
