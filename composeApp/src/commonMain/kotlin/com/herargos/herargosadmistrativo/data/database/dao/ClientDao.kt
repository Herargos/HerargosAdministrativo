package com.herargos.herargosadmistrativo.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.herargos.herargosadmistrativo.data.database.model.entity.ClientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createClient(client: ClientEntity)

    @Query("SELECT * FROM ClientEntity WHERE isAlive = true")
    fun getClients(): Flow<List<ClientEntity>>

    //@Delete
    @Query("UPDATE ClientEntity SET isAlive = false WHERE id = :client")
    suspend fun deleteClient(client: Int)

    @Update
    suspend fun updateClient(client: ClientEntity)
}