package com.herargos.herargosadmistrativo.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.herargos.herargosadmistrativo.data.database.model.entity.ProductEntity
import com.herargos.herargosadmistrativo.data.database.model.entity.RecipeEntity
import com.herargos.herargosadmistrativo.data.database.model.response.ProductWithRecipes
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    // --- Operaciones CRUD Básicas para ProductEntity ---
    @Insert
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun updateProduct(product: ProductEntity)

    //@Delete
    @Query("UPDATE ProductEntity SET isAlive = false WHERE id = :product")
    suspend fun deleteProduct(product: Int)

    @Query("SELECT * FROM ProductEntity WHERE isAlive = true")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE id = :productId")
    fun getProductById(productId: Int): Flow<ProductEntity?>

    // --- Operaciones CRUD Básicas para RecipeEntity (Necesarias para gestionar la relación) ---
    // (Podrías tener esto en RecipeDao, pero para operaciones transaccionales, es útil aquí)
    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert
    suspend fun insertAllRecipes(recipes: List<RecipeEntity>)

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM RecipeEntity WHERE idProduct = :productId")
    suspend fun deleteRecipesForProduct(productId: Int)

    // --- Operaciones Transaccionales para ProductWithRecipes ---

    /**
     * Inserta un nuevo producto y sus recetas asociadas.
     * Si el producto ya tiene un ID (no es autoGenerado), se usa ese ID para las recetas.
     * Si el producto es nuevo (ID = 0 y autoGenerado), el ID generado se usa para las recetas.
     */
    @Transaction
    suspend fun insertProductWithRecipes(productWithRecipes: ProductWithRecipes) {
        val productId = insertProduct(productWithRecipes.product) // Inserta el producto

        val recipesToInsert = productWithRecipes.recipes.map { recipe ->
            // Asegúrate de que las recetas tengan el ID del producto recién insertado
            recipe.copy(idProduct = productId.toInt())
        }
        insertAllRecipes(recipesToInsert) // Inserta todas las recetas asociadas
    }

    /**
     * Actualiza un producto y sus recetas.
     * Se elimina todas las recetas anteriores para ese producto y se insertan las nuevas.
     * Este enfoque simplifica la lógica de actualización, aunque podría ser menos eficiente
     * si solo cambian algunas recetas.
     */
    @Transaction
    suspend fun updateProductWithRecipes(productWithRecipes: ProductWithRecipes) {
        updateProduct(productWithRecipes.product) // Actualiza el producto

        // Primero, elimina todas las recetas antiguas asociadas con este producto
        deleteRecipesForProduct(productWithRecipes.product.id)

        // Luego, inserta las nuevas recetas. Asegúrate de que tengan el ID correcto del producto
        val recipesToInsert = productWithRecipes.recipes.map { recipe ->
            recipe.copy(idProduct = productWithRecipes.product.id)
        }
        insertAllRecipes(recipesToInsert)
    }

    /**
     * Elimina un producto y todas sus recetas asociadas.
     */
    @Transaction
    suspend fun deleteProductWithRecipes(productWithRecipes: ProductWithRecipes) {
        //deleteProduct(productWithRecipes.product) // Elimina el producto

        // También elimina todas las recetas asociadas a este producto
        //deleteRecipesForProduct(productWithRecipes.product.id)
    }

    // --- Métodos de Consulta para ProductWithRecipes ---
    @Transaction
    @Query("SELECT * FROM ProductEntity WHERE isAlive = true")
    fun getProductsWithRecipes(): Flow<List<ProductWithRecipes>>

    // Se cambia para que sea una función suspendida y devuelva un solo objeto
    @Transaction
    @Query("SELECT * FROM ProductEntity WHERE id = :productId")
    suspend fun getProductWithRecipesById(productId: Int): ProductWithRecipes?

    /**
     * Actualiza directamente el campo 'stock' para un producto específico.
     */
    @Query("UPDATE ProductEntity SET stock = :newStock WHERE id = :productId")
    suspend fun updateProductStock(productId: Int, newStock: Double)
}
