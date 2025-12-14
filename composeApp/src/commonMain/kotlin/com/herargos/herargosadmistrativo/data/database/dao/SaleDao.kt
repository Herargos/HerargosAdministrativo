package com.herargos.herargosadmistrativo.data.database.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.herargos.herargosadmistrativo.data.database.model.entity.ClientEntity
import com.herargos.herargosadmistrativo.data.database.model.entity.SaleEntity
import com.herargos.herargosadmistrativo.data.database.model.entity.SaleLineEntity
import com.herargos.herargosadmistrativo.data.database.model.response.SaleWithLines
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {

    // --- Operaciones SELECT (actualizadas para Flow y isAlive = 1) ---

    @Transaction
    @Query("SELECT * FROM SaleEntity WHERE isAlive = 1")
    fun getAllSalesWithLines(): Flow<List<SaleWithLines>> // Cambiado a Flow

    @Transaction
    @Query("SELECT * FROM SaleEntity WHERE id = :saleId AND isAlive = 1")
    fun getSaleWithLinesById(saleId: Int): Flow<SaleWithLines> // Cambiado a Flow

    @Transaction
    @Query("SELECT * FROM SaleEntity WHERE id = :saleId")
    fun getSaleWithLinesByIdAll(saleId: Int): Flow<SaleWithLines> // Cambiado a Flow

    @Query("SELECT * FROM SaleEntity WHERE isAlive = 1")
    fun getAllSaleEntities(): Flow<List<SaleEntity>> // Cambiado a Flow

    @Query("SELECT * FROM SaleEntity WHERE id = :saleId AND isAlive = 1")
    fun getSaleEntityById(saleId: Int): Flow<SaleEntity?> // Cambiado a Flow

    @Query("SELECT * FROM SaleLineEntity WHERE idSale = :saleId")
    fun getSaleLinesForSale(saleId: Int): Flow<List<SaleLineEntity>> // Cambiado a Flow

    @Query("SELECT * FROM ClientEntity WHERE id = :clientId AND isAlive = 1")
    fun getClientById(clientId: Int): Flow<ClientEntity?> // Cambiado a Flow

    // --- Operaciones INSERT (sin cambios) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SaleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleLines(saleLines: List<SaleLineEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: ClientEntity): Long

    @Transaction
    suspend fun insertSaleWithLines(saleWithLines: SaleWithLines): Long {
        val clientId = insertClient(saleWithLines.client)
        val saleId = insertSale(saleWithLines.sale.copy(idClient = clientId.toInt()))
        val saleLinesWithSaleId = saleWithLines.lines.map { it.copy(idSale = saleId.toInt()) }
        insertSaleLines(saleLinesWithSaleId)
        return saleId
    }

    // --- Operaciones UPDATE (sin cambios) ---

    @Update
    suspend fun updateSale(sale: SaleEntity)

    @Update
    suspend fun updateSaleLines(saleLines: List<SaleLineEntity>)

    @Update
    suspend fun updateClient(client: ClientEntity)

    @Transaction
    suspend fun updateSaleWithLines(saleWithLines: SaleWithLines) {
        updateClient(saleWithLines.client)
        updateSale(saleWithLines.sale)
        deleteSaleLinesBySaleId(saleWithLines.sale.id)
        insertSaleLines(saleWithLines.lines.map { it.copy(idSale = saleWithLines.sale.id) })
    }

    // --- Operaciones DELETE (soft delete como se definió antes) ---

    @Query("UPDATE SaleEntity SET isAlive = false WHERE id = :saleId")
    suspend fun softDeleteSale(saleId: Int)

    @Delete
    suspend fun deleteSaleLine(saleLine: SaleLineEntity)

    @Query("DELETE FROM SaleLineEntity WHERE idSale = :saleId")
    suspend fun deleteSaleLinesBySaleId(saleId: Int)

    @Query("UPDATE ClientEntity SET isAlive = 0 WHERE id = :clientId")
    suspend fun softDeleteClient(clientId: Int)

    @Transaction
    suspend fun deleteSaleWithLines(saleWithLines: SaleWithLines) {
        deleteSaleLinesBySaleId(saleWithLines.sale.id)
        softDeleteSale(saleWithLines.sale.id)
    }

    @Query("SELECT SUM(totalPrice) FROM SaleEntity WHERE DATE(createDate) = DATE(:todayDate) AND isAlive = 1")
    fun getTotalPriceToday(todayDate: String): Flow<Double?>

    @Query("SELECT SUM(totalPrice) FROM SaleEntity WHERE createDate BETWEEN :startOfWeek AND :endOfWeek AND isAlive = 1")
    fun getTotalPriceThisWeek(startOfWeek: String, endOfWeek: String): Flow<Double?>

    @Query("SELECT SUM(totalPrice) FROM SaleEntity WHERE createDate BETWEEN :startOfMonth AND :endOfMonth AND isAlive = 1")
    fun getTotalPriceThisMonth(startOfMonth: String, endOfMonth: String): Flow<Double?>

    @Query("SELECT SUM(totalPrice) FROM SaleEntity WHERE createDate BETWEEN :startOfYear AND :endOfYear AND isAlive = 1")
    fun getTotalPriceThisYear(startOfYear: String, endOfYear: String): Flow<Double?>

    @Query("SELECT SUM(totalPrice) FROM SaleEntity WHERE isAlive = 1")
    fun getTotalPriceAbsolute(): Flow<Double?>
}