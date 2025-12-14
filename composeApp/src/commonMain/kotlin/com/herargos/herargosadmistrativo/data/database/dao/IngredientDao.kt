package com.herargos.herargosadmistrativo.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.herargos.herargosadmistrativo.data.database.model.entity.IngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createIngredient(ingredient: IngredientEntity)

    @Query("SELECT * FROM IngredientEntity WHERE isAlive = true")
    fun getIngredients(): Flow<List<IngredientEntity>>

    @Query("SELECT * FROM IngredientEntity WHERE isAlive = true and id = :id")
    suspend fun getIngredientById(id: Int): IngredientEntity

    //@Delete
    @Query("UPDATE IngredientEntity SET isAlive = false WHERE id = :ingredient")
    suspend fun deleteIngredient(ingredient: Int)

    @Update
    suspend fun updateIngredient(ingredient: IngredientEntity)

    @Query("UPDATE IngredientEntity SET stock = stock - :quantity WHERE id = :ingredientId")
    suspend fun updateIngredientStock(ingredientId: Int, quantity: Double)

    @Query("UPDATE IngredientEntity SET stock = stock + :quantity WHERE id = :ingredientId")
    suspend fun revertIngredientStock(ingredientId: Int, quantity: Double)
}