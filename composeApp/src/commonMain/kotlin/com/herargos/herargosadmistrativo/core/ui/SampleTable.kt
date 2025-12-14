package com.herargos.herargosadmistrativo.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.delete_item
import org.jetbrains.compose.resources.stringResource

// Extension functions que asumo tienes en tu proyecto
fun Double.toStringRounded(): String = String.format("%.2f", this)


@Composable
fun <T> SimpleTable(
    headers: List<String>,
    data: List<T>,
    modifier: Modifier = Modifier,
    // ¡CAMBIO CLAVE AQUÍ! columnContents ahora recibe el índice y el item.
    columnContents: List<@Composable (index: Int, item: T) -> Unit>,
    onDeleteItem: ((T) -> Unit)? = null,
    showDeleteButton: Boolean = false,
    showTotalRow: Boolean = true,
    totalRowContent: @Composable (() -> Unit)? = null
) {
    val headerBackgroundColor = MaterialTheme.colorScheme.secondaryContainer
    val rowEvenBackgroundColor = CardDefaults.cardColors().containerColor
    val rowOddBackgroundColor = MaterialTheme.colorScheme.surfaceVariant

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerBackgroundColor)
                .padding(vertical = 8.dp)
        ) {
            headers.forEach { header ->
                Text(
                    text = header,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(data) { index, item -> // Usamos 'index' y 'item' de itemsIndexed
                val rowBackgroundColor =
                    if (index % 2 == 0) rowEvenBackgroundColor else rowOddBackgroundColor

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowBackgroundColor)
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    columnContents.forEach { content ->
                        // ¡CAMBIO CLAVE AQUÍ! Pasamos 'index' y 'item' al content.
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            content(index, item)
                        }
                    }

                    if (showDeleteButton && onDeleteItem != null) {
                        IconButton(
                            modifier = Modifier.weight(1f),
                            onClick = { onDeleteItem(item) },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(Res.string.delete_item),
                                )
                            }
                        )
                    }
                }
            }
        }

        if (showTotalRow) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                totalRowContent?.invoke() ?: run {
                    Text(
                        text = "Total no especificado",
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}