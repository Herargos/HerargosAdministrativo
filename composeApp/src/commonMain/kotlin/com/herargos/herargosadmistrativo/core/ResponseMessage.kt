package com.herargos.herargosadmistrativo.core

import org.jetbrains.compose.resources.StringResource

data class ResponseMessage(
    val message: StringResource? = null,
    val description: String? = null,
)
