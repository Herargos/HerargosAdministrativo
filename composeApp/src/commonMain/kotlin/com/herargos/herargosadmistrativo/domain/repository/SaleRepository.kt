package com.herargos.herargosadmistrativo.domain.repository

import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.domain.model.Sale
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun getSales(): Flow<RequestState<List<Sale>>>
    fun getSale(id: Int): Flow<RequestState<Sale>>
    suspend fun createSale(sale: Sale): RequestState<Boolean>
    suspend fun updateSale(sale: Sale): RequestState<Boolean>
    suspend fun deleteSale(sale: Sale): RequestState<Boolean>

    suspend fun getSaleByIdSingle(id: Int): RequestState<Sale>

    fun getTotalSalesToday(): Flow<RequestState<Double?>>
    fun getTotalSalesThisWeek(): Flow<RequestState<Double?>>
    fun getTotalSalesThisMonth(): Flow<RequestState<Double?>>
    fun getTotalSalesThisYear(): Flow<RequestState<Double?>>
    fun getTotalSalesAbsolute(): Flow<RequestState<Double?>>
}