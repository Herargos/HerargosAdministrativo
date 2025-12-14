package com.herargos.herargosadmistrativo.data.database.model.response

import androidx.room.Embedded
import androidx.room.Relation
import com.herargos.herargosadmistrativo.data.database.model.entity.ClientEntity
import com.herargos.herargosadmistrativo.data.database.model.entity.SaleEntity
import com.herargos.herargosadmistrativo.data.database.model.entity.SaleLineEntity
import com.herargos.herargosadmistrativo.domain.model.Sale

data class SaleWithLines(
    @Embedded
    val sale: SaleEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "idSale"
    )
    val lines: List<SaleLineEntity>,
    @Relation(
        parentColumn = "idClient",
        entityColumn = "id"
    )
    val client: ClientEntity
) {
    fun toDomain(): Sale {
        return Sale(
            id = sale.id,
            idClient = sale.idClient,
            client = client.toDomain(),
            totalPrice = sale.totalPrice.toString(),
            createDate = sale.createDate,
            lines = lines.map { it.toDomain() }
        )
    }
}
