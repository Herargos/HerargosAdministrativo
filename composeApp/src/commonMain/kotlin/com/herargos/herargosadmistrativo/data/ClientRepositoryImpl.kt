package com.herargos.herargosadmistrativo.data

import co.touchlab.kermit.Logger
import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.core.ResponseMessage
import com.herargos.herargosadmistrativo.data.database.AppDatabase
import com.herargos.herargosadmistrativo.domain.repository.ClientRepository
import com.herargos.herargosadmistrativo.domain.model.Client
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.error
import herargosadministrativo.composeapp.generated.resources.unknown_error_create_client
import herargosadministrativo.composeapp.generated.resources.unknown_error_delete_client
import herargosadministrativo.composeapp.generated.resources.unknown_error_update_client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ClientRepositoryImpl(private val dbHelper: AppDatabase) : ClientRepository {
    override suspend fun createClient(client: Client): RequestState<Boolean> {
        return try {
            dbHelper.clientDao().createClient(client.toEntity())
            RequestState.Success(data = true)
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error_create_client))
        }
    }

    override suspend fun getClients(): Flow<RequestState<List<Client>>> = flow {
        try {
            emit(RequestState.Loading)
            dbHelper.clientDao().getClients().collect { result ->
                emit(RequestState.Success(data = result.map { it.toDomain() }))
            }
        } catch (e: Exception) {
            emit(RequestState.Error(ResponseMessage(Res.string.error)))
        }

    }

    override suspend fun deleteClient(client: Client): RequestState<Boolean> {
        try {
            dbHelper.clientDao().deleteClient(client.toEntity().id)
            return RequestState.Success(data = true)
        } catch (e: Exception) {
            Logger.e(e) { "Error al eliminar cliente: ${client.name}" }
            return RequestState.Error(ResponseMessage(message = Res.string.unknown_error_delete_client))
        }
    }

    override suspend fun updateClient(client: Client): RequestState<Boolean> {
        try {
            dbHelper.clientDao().updateClient(client.toEntity())
            return RequestState.Success(data = true)
        } catch (e: Exception) {
            Logger.e(e) { "Error al actualizar cliente: ${client.name}" }
            return RequestState.Error(ResponseMessage(message = Res.string.unknown_error_update_client))
        }
    }
}