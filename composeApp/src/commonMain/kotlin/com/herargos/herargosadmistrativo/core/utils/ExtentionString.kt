package com.herargos.herargosadmistrativo.core.utils

import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.error_num_empty
import herargosadministrativo.composeapp.generated.resources.error_num_invalid_format
import herargosadministrativo.composeapp.generated.resources.error_num_max_value_double
import herargosadministrativo.composeapp.generated.resources.error_num_max_value_int
import herargosadministrativo.composeapp.generated.resources.error_num_min_value_double
import herargosadministrativo.composeapp.generated.resources.error_num_min_value_int
import herargosadministrativo.composeapp.generated.resources.error_text_empty
import herargosadministrativo.composeapp.generated.resources.error_text_forbidden_chars
import herargosadministrativo.composeapp.generated.resources.error_text_invalid_chars
import herargosadministrativo.composeapp.generated.resources.error_text_max_length
import herargosadministrativo.composeapp.generated.resources.error_text_min_length
import org.jetbrains.compose.resources.StringResource
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun String.isError(minLength: Int = 3, maxLength: Int = 50): StringResource? {
    val pattern = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s0-9]+\$")
    val forbiddenCharsPattern = Regex("[!@#$%^&*()]")

    if (this.isEmpty()) {
        return Res.string.error_text_empty
    }
    if (this.length < minLength) {
        return Res.string.error_text_min_length
    }
    if (this.length > maxLength) {
        return Res.string.error_text_max_length
    }
    if (!pattern.matches(this)) {
        return Res.string.error_text_invalid_chars
    }
    if (forbiddenCharsPattern.containsMatchIn(this)) {
        return Res.string.error_text_forbidden_chars
    }
    return null
}

fun String.isErrorDouble(minValue: Double = 0.01, maxValue: Double = 1000000.0): StringResource? {
    if (this.isEmpty()) {
        return Res.string.error_num_empty
    }
    val decimalFormatRegex = "^\\d+(\\.\\d{1,2})?$".toRegex()
    if (!this.matches(decimalFormatRegex)) {
        return Res.string.error_num_invalid_format
    }
    /*if (this.toDoubleOrNull() == null) {
        return Res.string.error_num_invalid_format
    }*/
    if (this.toDouble() < minValue) {
        return Res.string.error_num_min_value_double
    }
    if (this.toDouble() > maxValue) {
        return Res.string.error_num_max_value_double
    }
    return null
}

fun String.isErrorInt(minValue: Int = 1, maxValue: Int = 10000000): StringResource? {
    if (this.isEmpty()) {
        return Res.string.error_num_empty
    }
    val intFormatRegex = "^\\d+$".toRegex() // Expresión regular para enteros
    if (!this.matches(intFormatRegex)) {
        return Res.string.error_num_invalid_format // O un mensaje de error específico para enteros
    }
    /*if (this.toDoubleOrNull() == null) {
        return Res.string.error_num_invalid_format
    }*/
    if (this.toDouble() < minValue) {
        return Res.string.error_num_min_value_int
    }
    if (this.toDouble() > maxValue) {
        return Res.string.error_num_max_value_int
    }
    return null
}

fun String.formatNumberInt(): String {

    val number = this.toInt()

    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }

    val formatter = DecimalFormat("#,##0", symbols)

    return formatter.format(number)
}