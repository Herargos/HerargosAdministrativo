package com.herargos.herargosadmistrativo.core.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun OutlinedTextFieldDecimalComponent(
    modifier: Modifier = Modifier,
    value: Double,
    onValueChange: (Double?) -> Unit,
    label: @Composable () -> Unit,
    readOnly: Boolean = false
) {
    val aux = if (value == 0.0) "" else value.toString()
    var textValue = TextFieldValue(text = aux)
    var hasDecimalPoint by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        label = { label() },
        value = textValue,
        onValueChange = { newValue ->
            // Solo permitimos dígitos y punto decimal
            val allowedCharacters = Regex("^[0-9.]*$")
            if (newValue.text.matches(allowedCharacters)) {
                textValue = newValue
                if (newValue.text.contains(".")) {
                    hasDecimalPoint = true
                } else if (!newValue.text.contains(".") && hasDecimalPoint) {
                    hasDecimalPoint = false
                }

                // Convierte el valor del campo de texto a Double o null si está vacío
                val number = if (textValue.text.isNotEmpty()) {
                    try {
                        textValue.text.toDouble()
                    } catch (e: NumberFormatException) {
                        null
                    }
                } else {
                    null
                }
                onValueChange(number)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        readOnly = readOnly
    )
}