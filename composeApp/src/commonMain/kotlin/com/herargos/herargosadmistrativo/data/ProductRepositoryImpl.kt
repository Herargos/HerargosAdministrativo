package com.herargos.herargosadmistrativo.data

import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.core.ResponseMessage
import com.herargos.herargosadmistrativo.data.database.AppDatabase
import com.herargos.herargosadmistrativo.domain.repository.ProductRepository
import com.herargos.herargosadmistrativo.domain.model.Product
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.insufficient_stock
import herargosadministrativo.composeapp.generated.resources.unknown_error
import herargosadministrativo.composeapp.generated.resources.unknown_error_create_ingredient
import herargosadministrativo.composeapp.generated.resources.unknown_error_delete_ingredient
import herargosadministrativo.composeapp.generated.resources.unknown_error_update_ingredient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepositoryImpl(private val dbHelper: AppDatabase) : ProductRepository {
    override fun getProducts(): Flow<RequestState<List<Product>>> = flow {
        try {
            emit(RequestState.Loading)
            dbHelper.productDao().getProductsWithRecipes().collect { result ->
                emit(RequestState.Success(result.map { it.toDomain() }))
            }
        } catch (e: Exception) {
            emit(RequestState.Error(ResponseMessage(message = Res.string.unknown_error)))
        }
    }

    override suspend fun createProduct(product: Product): RequestState<Boolean> {
        return try {
            dbHelper.productDao().insertProductWithRecipes(product.toProductWithRecipes())
            RequestState.Success(data = true)
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error_create_ingredient))
        }
    }

    override suspend fun updateProduct(product: Product): RequestState<Boolean> {
        return try {
            dbHelper.productDao().updateProductWithRecipes(product.toProductWithRecipes())
            RequestState.Success(data = true)
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error_update_ingredient))
        }
    }

    override suspend fun deleteProduct(product: Product): RequestState<Boolean> {
        return try {
            dbHelper.productDao().deleteProduct(product.toProductWithRecipes().product.id)
            RequestState.Success(data = true)
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error_delete_ingredient))
        }
    }

    override suspend fun getProductById(id: Int): RequestState<Product> {
        return try {
            val productWithRecipes = dbHelper.productDao().getProductWithRecipesById(id)
            if (productWithRecipes != null) {
                RequestState.Success(data = productWithRecipes.toDomain())
            } else {
                // Puedes manejar un caso de error si el producto no se encuentra
                RequestState.Error(ResponseMessage(message = Res.string.unknown_error))
            }
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error))
        }
    }

    override suspend fun decreaseProductStock(productId: Int, quantity: Double): RequestState<Boolean> {
        return try {
            val productResult = getProductById(productId)

            if (productResult is RequestState.Error) {
                // El producto no existe o hubo un error al buscarlo.
                return productResult
            }
            val product = (productResult as RequestState.Success).data
            val currentStock = product.stock.toDoubleOrNull() ?: 0.0

            if (currentStock < quantity) {
                return RequestState.Error(
                    ResponseMessage(message = Res.string.insufficient_stock)
                )
            }

            val newStock = currentStock - quantity

            // Asume que dbHelper.productDao() tiene el método updateProductStock
            dbHelper.productDao().updateProductStock(productId, newStock)

            RequestState.Success(data = true)
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error))
        }
    }

    override suspend fun increaseProductStock(productId: Int, quantity: Double): RequestState<Boolean> {
        return try {
            val productResult = getProductById(productId)

            if (productResult is RequestState.Error) {
                // El producto no existe o hubo un error al buscarlo.
                return productResult
            }
            val product = (productResult as RequestState.Success).data
            val currentStock = product.stock.toDoubleOrNull() ?: 0.0

            val newStock = currentStock + quantity

            // Asume que dbHelper.productDao() tiene el método updateProductStock
            dbHelper.productDao().updateProductStock(productId, newStock)

            RequestState.Success(data = true)
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error))
        }
    }
}
