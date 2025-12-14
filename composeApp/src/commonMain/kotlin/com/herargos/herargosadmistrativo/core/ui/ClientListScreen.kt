package com.herargos.herargosadmistrativo.core.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herargos.herargosadmistrativo.core.utils.formatNumberInt
import com.herargos.herargosadmistrativo.domain.model.Client
import com.herargos.herargosadmistrativo.ui.sales.SaleEvent
import com.herargos.herargosadmistrativo.ui.sales.SaleViewModel
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.add
import herargosadministrativo.composeapp.generated.resources.app_icon
import herargosadministrativo.composeapp.generated.resources.clients
import herargosadministrativo.composeapp.generated.resources.delete
import herargosadministrativo.composeapp.generated.resources.more_options
import herargosadministrativo.composeapp.generated.resources.name_app
import herargosadministrativo.composeapp.generated.resources.search
import herargosadministrativo.composeapp.generated.resources.update
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientListScreen(
    clients: List<Client>,
    search: String,
    viewModel: SaleViewModel,
    update: (Client) -> Unit,
    delete: (Client) -> Unit,
    pick: (Client) -> Unit
) {
    val groupedClients = clients
        .filter {
            it.name.contains(search, ignoreCase = true) ||
                    it.identityCard.contains(search, ignoreCase = true)
        } // <-- Modificado para incluir búsqueda por DNI
        .groupBy { it.initial }
        .toSortedMap()

    Scaffold(
        topBar = {
            Column(
                // modifier = Modifier.background(MaterialTheme.colorScheme.surface) // Fondo para toda la top bar
            ) {
                // Search Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = search,
                        onValueChange = { viewModel.onEvent(SaleEvent.OnSearchClient(it)) },
                        placeholder = { Text(text = stringResource(Res.string.search)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = stringResource(Res.string.search)
                            )
                        },
                        modifier = Modifier
                            .weight(1f),
                        //.heightIn(min = 50.dp, max = 50.dp), // Altura fija
                        shape = RoundedCornerShape(25.dp), // Forma más redondeada ,
                        singleLine = true // Asegura que solo haya una línea
                    )
                    Spacer(Modifier.width(8.dp))
                    FloatingActionButton(
                        onClick = { viewModel.onEvent(SaleEvent.OnDisplayAddClient()) },
                        shape = CircleShape,
                        modifier = Modifier.size(48.dp) // Tamaño ligeramente ajustado
                    ) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(Res.string.add))
                    }
                    Spacer(Modifier.width(8.dp))
                    // More options icon
                    Box(
                        modifier = Modifier
                            .size(36.dp) // Tamaño del área del icono para que se vea como un botón circular
                            .clip(CircleShape)
                            .clickable(onClick = {})
                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)), // Fondo ligeramente coloreado
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.MoreVert, // Icono de "más opciones" horizontal
                            contentDescription = stringResource(Res.string.more_options),
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }
                }

                // User Profile Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp), // Más padding vertical
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hugo Verweij Avatar
                    Image(
                        painter = painterResource(Res.drawable.app_icon),
                        contentDescription = stringResource(Res.string.name_app),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp) // Un poco más grande para el avatar principal
                            .clip(CircleShape)
                    )
                    /*AsyncImage(
                        model = "https://randomuser.me/api/portraits/men/5.jpg", // Un avatar de ejemplo para Hugo
                        contentDescription = "Hugo Verweij",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp) // Un poco más grande para el avatar principal
                            .clip(CircleShape)
                    )*/
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.name_app),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp, // Tamaño de fuente un poco más grande
                        )
                        Row {
                            Text(
                                text = clients.size.toString() + " " + stringResource(Res.string.clients),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 16.dp) // Padding inferior para que el último elemento no esté pegado
            ) {
                groupedClients.forEach { (initial, clientsForInitial) ->
                    stickyHeader {
                        ClientSectionHeader(initial)
                    }
                    items(clientsForInitial, key = { it.id }) { client ->
                        ClientListItem(client, update, delete, pick)
                    }
                }
            }

            // Fast scroll alphabet index (on the right)
            /*Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(
                        end = 8.dp,
                        top = paddingValues.calculateTopPadding() + 8.dp,
                        bottom = 8.dp
                    ) // Ajuste de padding
                    .background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                ('A'..'Z').forEach { char ->
                    Text(
                        text = char.toString(),
                        fontSize = 10.sp, // Letras más pequeñas en el índice
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            // TODO: Implement actual scroll to section functionality here
                            println("Clicked on $char")
                        }
                    )
                }
            }*/
        }
    }
}

@Composable
fun ClientSectionHeader(initial: String) {
    Text(
        text = initial,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp, // Tamaño de fuente del encabezado
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun ClientListItem(
    client: Client,
    update: (Client) -> Unit,
    delete: (Client) -> Unit,
    pick: (Client) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { pick(client) }
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            ), // Más padding vertical para cada elemento
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Contact Avatar or Initial Circle
        Box(
            modifier = Modifier
                .size(50.dp) // Consistente con el tamaño del avatar
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)), // Fondo suave para las iniciales
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = client.initial,
                color = MaterialTheme.colorScheme.primary, // Color de las iniciales
                fontWeight = FontWeight.SemiBold, // Un poco más de peso
                fontSize = 18.sp
            )
        }


        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) { // Usar Column para nombre y DNI
            Text(
                text = client.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "DNI: ${client.identityCard.formatNumberInt()}", // Formato "DNI: [número]"
                fontSize = 12.sp,
                color = Color.Gray // Color más discreto para el DNI
            )
        }
        Spacer(Modifier.width(8.dp))
        // Action Icons
        ClientActionIcon(
            icon = Icons.Default.Edit,
            contentDescription = stringResource(Res.string.update)
        ) { update(client) }
        Spacer(Modifier.width(12.dp)) // Espaciado entre iconos
        ClientActionIcon(
            icon = Icons.Default.Delete,
            contentDescription = stringResource(Res.string.delete)
        ) { delete(client) }
    }
}

@Composable
fun ClientActionIcon(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    // Aquí puedes aplicar el estilo de la imagen original si quieres un círculo alrededor del icono
    Box(
        modifier = Modifier
            .size(36.dp) // Tamaño del área del icono para que se vea como un botón circular
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)), // Fondo ligeramente coloreado
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSecondaryContainer, // Color del icono
            modifier = Modifier.size(20.dp) // Tamaño del icono dentro del círculo
        )
    }
}