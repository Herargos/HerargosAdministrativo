package com.herargos.herargosadmistrativo.domain.model

import com.herargos.herargosadmistrativo.data.database.model.entity.SaleLineEntity

data class SaleLine(
    val id: Int = 0,
    val idSale: Int = 0,
    val idProduct: Int = 0,
    val productName: String = "",
    val productStock: Double = 0.0,
    val singlePrice: Double = 0.0,
    val totalPrice: Double = 0.0
) {
    fun toEntity(): SaleLineEntity {
        return SaleLineEntity(
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
