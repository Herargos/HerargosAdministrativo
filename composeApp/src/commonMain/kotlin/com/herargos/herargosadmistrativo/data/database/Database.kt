package com.herargos.herargosadmistrativo.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.herargos.herargosadmistrativo.data.database.dao.ClientDao
import com.herargos.herargosadmistrativo.data.database.dao.IngredientDao
import com.herargos.herargosadmistrativo.data.database.dao.ProductDao
import com.herargos.herargosadmistrativo.data.database.dao.RecipeDao
import com.herargos.herargosadmistrativo.data.database.dao.SaleDao
import com.herargos.herargosadmistrativo.data.database.dao.SaleLineDao
import com.herargos.herargosadmistrativo.data.database.model.entity.ClientEntity
import com.herargos.herargosadmistrativo.data.database.model.entity.IngredientEntity
import com.herargos.herargosadmistrativo.data.database.model.entity.ProductEntity
import com.herargos.herargosadmistrativo.data.database.model.entity.RecipeEntity
import com.herargos.herargosadmistrativo.data.database.model.entity.SaleEntity
import com.herargos.herargosadmistrativo.data.database.model.entity.SaleLineEntity

const val DATABASE_NAME = "app.db"

@Database(
    entities = [
        ClientEntity::class,
        IngredientEntity::class,
        ProductEntity::class,
        RecipeEntity::class,
        SaleEntity::class,
        SaleLineEntity::class
    ],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun clientDao(): ClientDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun productDao(): ProductDao
    abstract fun recipeDao(): RecipeDao
    abstract fun saleDao(): SaleDao
    abstract fun saleLineDao(): SaleLineDao
    override fun clearAllTables(): Unit {}
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

interface DB {
    fun clearAllTables(): Unit {}
}