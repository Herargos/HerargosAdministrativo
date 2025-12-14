package com.herargos.herargosadmistrativo.core.utils

import com.herargos.herargosadmistrativo.core.ResponseMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface Messages {
    val messages: Flow<ResponseMessage>

    suspend fun sendMessage(code: ResponseMessage)
}

class DefaultMessages : Messages {
    private val _messages = Channel<ResponseMessage>()
    override val messages: Flow<ResponseMessage> = _messages.receiveAsFlow()

    override suspend fun sendMessage(code: ResponseMessage) {
        _messages.send(code)
    }
}