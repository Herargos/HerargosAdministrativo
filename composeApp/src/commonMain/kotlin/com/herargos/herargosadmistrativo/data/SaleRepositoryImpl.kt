package com.herargos.herargosadmistrativo.data

import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.core.ResponseMessage
import com.herargos.herargosadmistrativo.core.utils.getEndOfMonthFormatted
import com.herargos.herargosadmistrativo.core.utils.getEndOfWeekFormatted
import com.herargos.herargosadmistrativo.core.utils.getEndOfYearFormatted
import com.herargos.herargosadmistrativo.core.utils.getStartOfMonthFormatted
import com.herargos.herargosadmistrativo.core.utils.getStartOfWeekFormatted
import com.herargos.herargosadmistrativo.core.utils.getStartOfYearFormatted
import com.herargos.herargosadmistrativo.core.utils.getTodayDateFormatted
import com.herargos.herargosadmistrativo.data.database.AppDatabase
import com.herargos.herargosadmistrativo.domain.model.Sale
import com.herargos.herargosadmistrativo.domain.repository.SaleRepository
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.unknown_error
import herargosadministrativo.composeapp.generated.resources.unknown_error_create_sale
import herargosadministrativo.composeapp.generated.resources.unknown_error_delete_sale
import herargosadministrativo.composeapp.generated.resources.unknown_error_update_sale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class SaleRepositoryImpl(private val dbHelper: AppDatabase) : SaleRepository {
    override fun getSales(): Flow<RequestState<List<Sale>>> = flow {
        try {
            emit(RequestState.Loading)
            dbHelper.saleDao().getAllSalesWithLines().collect { result ->
                emit(RequestState.Success(result.map { it.toDomain() }))
            }
        } catch (e: Exception) {
            emit(
                RequestState.Error(
                    ResponseMessage(
                        message = Res.string.unknown_error,
                        description = e.localizedMessage
                    )
                )
            )
        }
    }

    override fun getSale(id: Int): Flow<RequestState<Sale>> = flow {
        try {
            emit(RequestState.Loading)
            dbHelper.saleDao().getSaleWithLinesByIdAll(id).collect { result ->
                emit(RequestState.Success(result.toDomain()))
            }
        } catch (e: Exception) {
            emit(
                RequestState.Error(
                    ResponseMessage(
                        message = Res.string.unknown_error,
                        description = e.localizedMessage
                    )
                )
            )
        }
    }

    override suspend fun createSale(sale: Sale): RequestState<Boolean> {
        return try {
            dbHelper.saleDao().insertSaleWithLines(sale.toSaleWithLines())
            RequestState.Success(data = true)
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error_create_sale))
        }
    }

    override suspend fun updateSale(sale: Sale): RequestState<Boolean> {
        return try {
            dbHelper.saleDao().updateSaleWithLines(sale.toSaleWithLines())
            RequestState.Success(data = true)
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error_update_sale))
        }
    }

    override suspend fun deleteSale(sale: Sale): RequestState<Boolean> {
        return try {
            dbHelper.saleDao().softDeleteSale(sale.toSaleWithLines().sale.id)
            RequestState.Success(data = true)
        } catch (e: Exception) {
            RequestState.Error(
                ResponseMessage(
                    message = Res.string.unknown_error_delete_sale,
                    description = e.message
                )
            )
        }
    }

    override suspend fun getSaleByIdSingle(id: Int): RequestState<Sale> {
        return try {
            // Usamos .first() para tomar el primer valor del Flow y terminar la recolección
            val saleWithLines = dbHelper.saleDao().getSaleWithLinesById(id).first()
            RequestState.Success(data = saleWithLines.toDomain())
        } catch (e: Exception) {
            // Esto también captura excepciones si el SaleWithLines es null o si falla la BD
            RequestState.Error(
                ResponseMessage(
                    message = Res.string.unknown_error,
                    description = "Error fetching single Sale by ID: ${e.localizedMessage}"
                )
            )
        }
    }

    override fun getTotalSalesToday(): Flow<RequestState<Double?>> = flow {
        emit(RequestState.Loading)
        try {
            val todayDate = getTodayDateFormatted()
            dbHelper.saleDao().getTotalPriceToday(todayDate)
                .collect { total ->
                    emit(RequestState.Success(total))
                }
        } catch (e: Exception) {
            emit(
                RequestState.Error(
                    ResponseMessage(
                        message = Res.string.unknown_error,
                        description = e.localizedMessage
                    )
                )
            )
        }
    }

    override fun getTotalSalesThisWeek(): Flow<RequestState<Double?>> = flow {
        emit(RequestState.Loading)
        try {
            val startOfWeek = getStartOfWeekFormatted()
            val endOfWeek = getEndOfWeekFormatted()
            dbHelper.saleDao().getTotalPriceThisWeek(startOfWeek, endOfWeek)
                .collect { total ->
                    emit(RequestState.Success(total))
                }
        } catch (e: Exception) {
            emit(
                RequestState.Error(
                    ResponseMessage(
                        message = Res.string.unknown_error,
                        description = e.localizedMessage
                    )
                )
            )
        }
    }

    override fun getTotalSalesThisMonth(): Flow<RequestState<Double?>> = flow {
        emit(RequestState.Loading)
        try {
            val startOfMonth = getStartOfMonthFormatted()
            val endOfMonth = getEndOfMonthFormatted()
            dbHelper.saleDao().getTotalPriceThisMonth(startOfMonth, endOfMonth)
                .collect { total ->
                    emit(RequestState.Success(total))
                }
        } catch (e: Exception) {
            emit(
                RequestState.Error(
                    ResponseMessage(
                        message = Res.string.unknown_error,
                        description = e.localizedMessage
                    )
                )
            )
        }
    }

    override fun getTotalSalesThisYear(): Flow<RequestState<Double?>> = flow {
        emit(RequestState.Loading)
        try {
            val startOfYear = getStartOfYearFormatted()
            val endOfYear = getEndOfYearFormatted()
            dbHelper.saleDao().getTotalPriceThisYear(startOfYear, endOfYear)
                .collect { total ->
                    emit(RequestState.Success(total))
                }
        } catch (e: Exception) {
            emit(
                RequestState.Error(
                    ResponseMessage(
                        message = Res.string.unknown_error,
                        description = e.localizedMessage
                    )
                )
            )
        }
    }

    override fun getTotalSalesAbsolute(): Flow<RequestState<Double?>> = flow {
        emit(RequestState.Loading)
        try {
            dbHelper.saleDao().getTotalPriceAbsolute()
                .collect { total ->
                    emit(RequestState.Success(total))
                }
        } catch (e: Exception) {
            emit(
                RequestState.Error(
                    ResponseMessage(
                        message = Res.string.unknown_error,
                        description = e.localizedMessage
                    )
                )
            )
        }
    }
}