package com.herargos.herargosadmistrativo.domain.model

import com.herargos.herargosadmistrativo.data.database.model.entity.SaleEntity
import com.herargos.herargosadmistrativo.data.database.model.response.SaleWithLines

data class Sale(
    val id: Int = 0,
    val idClient: Int = 0,
    val client: Client = Client(),
    val totalPrice: String = "",
    val createDate: String = "",
    val lines: List<SaleLine> = listOf()
) {
    fun toSaleWithLines(): SaleWithLines {
        return SaleWithLines(
            sale = this.toEntity(),
            lines = lines.map { it.toEntity() },
            client = client.toEntity()
        )
    }

    private fun toEntity(): SaleEntity {
        return SaleEntity(
            id = id,
            idClient = idClient,
            totalPrice = totalPrice.toDouble(),
            createDate = createDate
        )
    }
}
