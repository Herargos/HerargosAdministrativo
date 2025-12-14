package com.herargos.herargosadmistrativo.data.database.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.herargos.herargosadmistrativo.domain.model.Client

@Entity(indices = [Index(value = ["identityCard"], unique = true)])
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val identityCard: String = "",
    val name: String = "",
    val isAlive: Boolean = true
) {
    fun toDomain(): Client {
        return Client(
            id = id,
            identityCard = identityCard,
            name = name
        )
    }
}
