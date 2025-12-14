package com.herargos.herargosadmistrativo.core.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.error_invalid_number_format
import org.jetbrains.compose.resources.stringResource


@Composable
fun DoubleInputField(
    value: Double,
    onValueChange: (Double) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isErrorText: Boolean = false,
    supportingText: @Composable () -> Unit? = {}
) {
    var textState by remember(value) { mutableStateOf(if (value == 0.0) "" else value.toString()) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(value) {
        val newText = if (value == 0.0) "" else value.toString()
        if (textState != newText && textState.toDoubleOrNull() != value) {
            textState = newText
        }
    }

    OutlinedTextField(
        value = textState,
        onValueChange = { newText ->
            val decimalPointIndex = newText.indexOf('.')
            val hasDecimalPoint = decimalPointIndex != -1

            val filteredText = newText.filterIndexed { index, char ->
                char.isDigit() || (char == '.' && newText.indexOf('.') == index)
            }

            // Check for more than one decimal point
            if (filteredText.count { it == '.' } > 1) {
                isError = true
                return@OutlinedTextField // Don't update textState, ignore input
            }

            // Check for more than two decimal places after the point
            if (hasDecimalPoint && newText.length - 1 - decimalPointIndex > 2) {
                // If the new text has more than 2 decimal places, and the current text
                // already has 2 or less, it means the user typed an invalid character.
                // We keep the old textState.
                val currentDecimalPlaces =
                    if (textState.contains('.')) textState.substringAfter(".").length else 0
                if (currentDecimalPlaces <= 2) {
                    isError = true
                    return@OutlinedTextField
                }
            }


            textState = filteredText

            if (filteredText.isEmpty()) {
                onValueChange(0.0)
                isError = false
            } else {
                val parsedValue = filteredText.toDoubleOrNull()
                if (parsedValue != null && parsedValue >= 0.0) {
                    onValueChange(parsedValue)
                    isError = false
                } else {
                    isError = true
                }
            }
        },
        label = { Text(label) },
        isError = isError || isErrorText,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        supportingText = {
            if (isError) {
                Text(stringResource(Res.string.error_invalid_number_format))
            }
            supportingText()
        },
        modifier = modifier,
        enabled = enabled
    )
}
