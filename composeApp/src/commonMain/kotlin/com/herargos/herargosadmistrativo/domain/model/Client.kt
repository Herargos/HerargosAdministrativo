package com.herargos.herargosadmistrativo.domain.model

import com.herargos.herargosadmistrativo.data.database.model.entity.ClientEntity

data class Client(
    val id: Int = 0,
    val identityCard: String = "",
    val name: String = "",
    val initial: String = name.firstOrNull()?.uppercase() ?: ""
) {
    fun toEntity(): ClientEntity {
        return ClientEntity(
            id = id,
            identityCard = identityCard,
            name = name
        )
    }
}
