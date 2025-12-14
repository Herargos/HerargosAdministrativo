package com.herargos.herargosadmistrativo.domain.repository

import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<RequestState<List<Product>>>
    suspend fun createProduct(product: Product): RequestState<Boolean>
    suspend fun updateProduct(product: Product): RequestState<Boolean>
    suspend fun deleteProduct(product: Product): RequestState<Boolean>
    suspend fun getProductById(id: Int): RequestState<Product>

    suspend fun decreaseProductStock(productId: Int, quantity: Double): RequestState<Boolean>
    suspend fun increaseProductStock(productId: Int, quantity: Double): RequestState<Boolean>
}