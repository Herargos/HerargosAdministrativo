package com.herargos.herargosadmistrativo.domain.repository

import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.domain.model.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    suspend fun createClient(client: Client): RequestState<Boolean>
    suspend fun getClients(): Flow<RequestState<List<Client>>>
    suspend fun deleteClient(client: Client): RequestState<Boolean>
    suspend fun updateClient(client: Client): RequestState<Boolean>
}