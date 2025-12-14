package com.herargos.herargosadmistrativo.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.herargos.herargosadmistrativo.ui.product.ProductState


// Mantén si usas DropdownMenu de Material (Compose 1.x)
@Composable
fun <T> AutoCompleteGeneric(
    modifier: Modifier = Modifier,
    items: List<T>,
    itemToString: (T) -> String,
    label: String,
    placeholder: String,
    onItemSelected: (T) -> Unit,
    searchText: String,
    onValueChange: (String) -> Unit
) {
    var selectedItem by remember { mutableStateOf<T?>(null) }
    //var categoryText by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                AutoCompleteTextField( // Este es el componente que modificaremos
                    value = searchText,
                    onValueChange = {
                        onValueChange(it)
                        expanded = true
                        selectedItem = null
                    },
                    placeholder = placeholder,
                    label = label,
                    textFieldSize = { size -> textFieldSize = size }
                ) {
                    expanded = !expanded
                }
            }

            // --- CAMBIO CLAVE AQUÍ: Usar AnimatedVisibility con Surface y Column en lugar de DropdownMenu ---
            AnimatedVisibility(visible = expanded) {
                Surface(
                    modifier = Modifier
                        .width(textFieldSize.width.dp) // Mantener el ancho del TextField
                        .padding(horizontal = 5.dp),
                    shape = androidx.compose.material3.MaterialTheme.shapes.medium, // O la forma que prefieras
                    shadowElevation = 4.dp // Para darle un poco de elevación como un menú
                ) {
                    val filteredItems = if (searchText.isNotEmpty()) {
                        items.filter {
                            itemToString(it).lowercase().contains(searchText.lowercase())
                        }.sortedBy { itemToString(it) }
                    } else {
                        items.sortedBy { itemToString(it) }
                    }

                    Column {
                        if (filteredItems.isEmpty()) {
                            // Mostrar un mensaje si no hay resultados
                            DropdownMenuItem(
                                onClick = { /* No-op */ },
                                enabled = false,
                                text = { Text("No se encontraron resultados") }
                            )
                        } else {
                            filteredItems.forEach { item ->
                                DropdownMenuItem(
                                    onClick = {
                                        onValueChange(itemToString(item)) // Actualizar el texto del TextField con el item seleccionado
                                        // selectedItem = item // No necesario aquí
                                        expanded = false // Cerrar el menú
                                        onItemSelected(item) // Notificar la selección al padre
                                    },
                                    text = {
                                        Text(itemToString(item))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Componentes de ayuda ---

// MODIFICACIÓN: Cambiado de TextField a OutlinedTextField
@Composable
fun AutoCompleteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    label: String,
    textFieldSize: (Size) -> Unit,
    onTrailingIconClick: () -> Unit
) {
    // CAMBIO CLAVE AQUÍ: Usar OutlinedTextField en lugar de TextField
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = onTrailingIconClick) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown arrow")
            }
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                textFieldSize(coordinates.size.toSize())
            }
    )
}

@Composable
fun <T> AutoCompleteDropdownGeneric(
    modifier: Modifier = Modifier,
    textFieldWidth: Dp,
    items: List<T>,
    itemToString: (T) -> String,
    onItemClick: (T) -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = { /* Manejado por el padre */ },
        modifier = modifier.width(textFieldWidth)
    ) {
        if (items.isEmpty()) {
            DropdownMenuItem(
                onClick = { /* No-op */ },
                enabled = false,
                text = { Text("No results found") }
            )
        } else {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = { onItemClick(item) },
                    text = {
                        Text(itemToString(item))
                    }
                )
            }
        }
    }
}
