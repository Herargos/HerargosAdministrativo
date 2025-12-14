package com.herargos.herargosadmistrativo.ui.sales

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herargos.herargosadmistrativo.core.ui.AlertDialogExample
import com.herargos.herargosadmistrativo.core.ui.AutoCompleteGeneric
import com.herargos.herargosadmistrativo.core.ui.ClientListScreen
import com.herargos.herargosadmistrativo.core.ui.SimpleTable
import com.herargos.herargosadmistrativo.core.utils.convertDateTimeFormat
import com.herargos.herargosadmistrativo.core.utils.formatNumberInt
import com.herargos.herargosadmistrativo.core.utils.toStringRounded
import com.herargos.herargosadmistrativo.domain.model.Sale
import com.herargos.herargosadmistrativo.domain.model.SaleLine
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.add
import herargosadministrativo.composeapp.generated.resources.arrow_back
import herargosadministrativo.composeapp.generated.resources.client
import herargosadministrativo.composeapp.generated.resources.close
import herargosadministrativo.composeapp.generated.resources.create
import herargosadministrativo.composeapp.generated.resources.create_client
import herargosadministrativo.composeapp.generated.resources.create_sale
import herargosadministrativo.composeapp.generated.resources.date
import herargosadministrativo.composeapp.generated.resources.delete
import herargosadministrativo.composeapp.generated.resources.delete_item
import herargosadministrativo.composeapp.generated.resources.delete_item_description
import herargosadministrativo.composeapp.generated.resources.delete_item_description_main
import herargosadministrativo.composeapp.generated.resources.error
import herargosadministrativo.composeapp.generated.resources.identity_card
import herargosadministrativo.composeapp.generated.resources.name
import herargosadministrativo.composeapp.generated.resources.new_sale
import herargosadministrativo.composeapp.generated.resources.of_the_day
import herargosadministrativo.composeapp.generated.resources.of_the_month
import herargosadministrativo.composeapp.generated.resources.of_the_week
import herargosadministrativo.composeapp.generated.resources.of_the_year
import herargosadministrativo.composeapp.generated.resources.price
import herargosadministrativo.composeapp.generated.resources.quantity
import herargosadministrativo.composeapp.generated.resources.revenue
import herargosadministrativo.composeapp.generated.resources.sale_module
import herargosadministrativo.composeapp.generated.resources.select_product
import herargosadministrativo.composeapp.generated.resources.show
import herargosadministrativo.composeapp.generated.resources.single_price
import herargosadministrativo.composeapp.generated.resources.stock
import herargosadministrativo.composeapp.generated.resources.success
import herargosadministrativo.composeapp.generated.resources.total
import herargosadministrativo.composeapp.generated.resources.total_price
import herargosadministrativo.composeapp.generated.resources.update
import herargosadministrativo.composeapp.generated.resources.update_client
import herargosadministrativo.composeapp.generated.resources.update_sale
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SaleScreen(viewModel: SaleViewModel = koinViewModel(), onBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            AlertDialogClient(
                displayClient = state.displayClient,
                viewModel = viewModel,
                state = state
            )
            AlertDialogAddClient(
                displayAddClient = state.displayAddClient,
                viewModel = viewModel,
                state = state
            )
            DialogDeleteClient(
                displayDeleteClient = state.displayDeleteClient,
                viewModel = viewModel,
                state = state
            )
            AlertDialogSale(
                displaySale = state.displaySale,
                viewModel = viewModel,
                state = state
            )
            AlertDialogSaleLine(
                displaySaleLine = state.displaySaleLine,
                viewModel = viewModel,
                state = state
            )
            AlertDialogSaleDetail(
                displaySaleLine = state.displaySaleDetail,
                viewModel = viewModel,
                state = state
            )
            AnimatedVisibility(state.displaySaleDelete) {
                AlertDialogExample(
                    onDismissRequest = { viewModel.onEvent(SaleEvent.OnDismissDelete) },
                    onConfirmation = { viewModel.onEvent(SaleEvent.OnDeleteSale(state.saleSelected)) },
                    dialogTitle = stringResource(Res.string.delete_item),
                    dialogText = stringResource(Res.string.delete_item_description_main),
                    icon = Res.drawable.delete
                )
            }
            AccountCard(viewModel, state, onBack)
            Spacer(modifier = Modifier.height(16.dp))

            TransactionsTable(
                sales = state.sales,
                modifier = Modifier.weight(1f),
                onItemSelected = { sale -> viewModel.onEvent(SaleEvent.OnDisplaySaleDetail(sale)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountCard(viewModel: SaleViewModel, state: SaleState, onBack: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onBack() },
                        content = {
                            Icon(
                                painter = painterResource(Res.drawable.arrow_back),
                                contentDescription = stringResource(Res.string.arrow_back),
                            )
                        }
                    )
                    Text(
                        text = stringResource(Res.string.sale_module),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        fontSize = 20.sp
                    )
                }
                Button(
                    onClick = { viewModel.onEvent(SaleEvent.OnDisplayClient) },
                    colors = ButtonDefaults.buttonColors(),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(Res.string.new_sale),
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(Res.string.revenue),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = stringResource(Res.string.of_the_day),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = state.saleToday.toStringRounded(),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(Res.string.revenue),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = stringResource(Res.string.of_the_week),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = state.saleWeek.toStringRounded(),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(Res.string.revenue),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = stringResource(Res.string.of_the_month),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = state.saleMonth.toStringRounded(),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(Res.string.revenue),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = stringResource(Res.string.of_the_year),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = state.saleYear.toStringRounded(),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(Res.string.revenue),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = stringResource(Res.string.total),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = state.saleEver.toStringRounded(),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionsTable(
    sales: List<Sale>,
    modifier: Modifier = Modifier,
    onItemSelected: (Sale) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            TransactionTableHeader()
            HorizontalDivider(thickness = 1.dp)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(sales) { index, sale ->
                    TransactionRow(
                        index = index,
                        sale = sale,
                        onItemSelected = { onItemSelected(it) })
                    if (index < sales.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "#",
            modifier = Modifier.weight(0.1f),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = stringResource(Res.string.client).uppercase(),
            modifier = Modifier.weight(0.3f),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = stringResource(Res.string.price).uppercase(),
            modifier = Modifier.weight(0.25f),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = stringResource(Res.string.date).uppercase(),
            modifier = Modifier.weight(0.25f),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.width(20.dp))
    }
}

@Composable
fun TransactionRow(
    index: Int,
    sale: Sale,
    onItemSelected: (Sale) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = (index + 1).toString(),
            modifier = Modifier.weight(0.1f),
            fontSize = 13.sp
        )
        Text(
            text = sale.client.name,
            modifier = Modifier.weight(0.3f),
            fontSize = 13.sp
        )
        Text(
            text = sale.totalPrice,
            modifier = Modifier.weight(0.25f),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = sale.createDate.convertDateTimeFormat(),
            modifier = Modifier.weight(0.25f),
            fontSize = 13.sp
        )
        IconButton(
            onClick = { onItemSelected(sale) },
            content = {
                Icon(
                    painter = painterResource(Res.drawable.show),
                    contentDescription = "Details",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        )
    }
}

@Composable
fun AlertDialogClient(displayClient: Boolean, viewModel: SaleViewModel, state: SaleState) {
    AnimatedVisibility(displayClient) {
        Dialog(onDismissRequest = { viewModel.onEvent(SaleEvent.OnDismissClient) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                ClientListScreen(
                    clients = state.clients.sortedBy { it.name },
                    search = state.searchClient,
                    viewModel = viewModel,
                    update = { client -> viewModel.onEvent(SaleEvent.OnDisplayAddClient(client = client)) },
                    delete = { client -> viewModel.onEvent(SaleEvent.OnDisplayDeleteClient(client = client)) },
                    pick = { client -> viewModel.onEvent(SaleEvent.OnDisplaySale(client = client)) }
                )
            }
        }
    }
}

@Composable
fun AlertDialogAddClient(displayAddClient: Boolean, viewModel: SaleViewModel, state: SaleState) {
    AnimatedVisibility(displayAddClient) {
        Dialog(onDismissRequest = { viewModel.onEvent(SaleEvent.OnDismissAddClient) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                FromClient(viewModel, state)
            }
        }
    }
}

@Composable
fun DialogDeleteClient(displayDeleteClient: Boolean, viewModel: SaleViewModel, state: SaleState) {
    AnimatedVisibility(displayDeleteClient) {
        AlertDialogExample(
            onDismissRequest = { viewModel.onEvent(SaleEvent.OnDismissDeleteClient) },
            onConfirmation = { viewModel.onEvent(SaleEvent.OnDeleteClient(state.client)) },
            dialogTitle = stringResource(Res.string.delete_item),
            dialogText = stringResource(Res.string.delete_item_description) + state.client.name,
            icon = Res.drawable.delete
        )
    }
}

@Composable
fun AlertDialogSale(displaySale: Boolean, viewModel: SaleViewModel, state: SaleState) {
    AnimatedVisibility(displaySale) {
        Dialog(onDismissRequest = { viewModel.onEvent(SaleEvent.OnDismissSale) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                FromSale(viewModel, state)
            }
        }
    }
}

@Composable
fun AlertDialogSaleLine(displaySaleLine: Boolean, viewModel: SaleViewModel, state: SaleState) {
    AnimatedVisibility(displaySaleLine) {
        Dialog(onDismissRequest = { viewModel.onEvent(SaleEvent.OnDismissSaleLines) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                val headersSaleLines = listOf(
                    "#",
                    stringResource(Res.string.name),
                    stringResource(Res.string.stock),
                    stringResource(Res.string.single_price),
                    stringResource(Res.string.total_price),
                    ""
                )

                val saleLinesColumnContents: List<@Composable (index: Int, item: SaleLine) -> Unit> =
                    listOf(
                        { index, _ ->
                            Text(
                                text = (index + 1).toString(),
                                textAlign = TextAlign.Center
                            )
                        },
                        { _, item ->
                            Text(
                                text = item.productName,
                                textAlign = TextAlign.Center
                            )
                        },
                        { _, item ->
                            Text(
                                text = item.productStock.toStringRounded(),
                                textAlign = TextAlign.Center
                            )
                        },
                        { _, item ->
                            Text(
                                text = item.singlePrice.toStringRounded(),
                                textAlign = TextAlign.Center
                            )
                        },
                        { _, item ->
                            Text(
                                text = item.totalPrice.toStringRounded(),
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                SimpleTable(
                    headers = headersSaleLines,
                    data = state.sale.lines,
                    columnContents = saleLinesColumnContents,
                    onDeleteItem = { item -> viewModel.onEvent(SaleEvent.OnDeleteItemList(item)) },
                    showDeleteButton = true,
                    showTotalRow = true,
                    totalRowContent = {
                        Text(
                            text = "${stringResource(Res.string.total)} ${
                                state.sale.lines.sumOf { it.totalPrice }.toStringRounded()
                            }",
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun AlertDialogSaleDetail(displaySaleLine: Boolean, viewModel: SaleViewModel, state: SaleState) {
    AnimatedVisibility(displaySaleLine) {
        Dialog(onDismissRequest = { viewModel.onEvent(SaleEvent.OnDismissSaleDetail) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(700.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                SaleDetail(
                    sale = state.saleSelected,
                    onDelete = { viewModel.onEvent(SaleEvent.OnDisplayDelete) },
                    onUpdate = { sale ->
                        viewModel.onEvent(
                            SaleEvent.OnDisplaySale(
                                client = sale.client,
                                sale = sale
                            )
                        )
                    })
            }
        }
    }
}

@Composable
fun SaleDetail(sale: Sale, onDelete: () -> Unit, onUpdate: (Sale) -> Unit) {
    Column {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                Row {
                    Text(
                        text = stringResource(Res.string.client) + ": ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = sale.client.name)
                }
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                Row {
                    Text(
                        text = stringResource(Res.string.identity_card) + ": ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = sale.client.identityCard)
                }
            }
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                Row {
                    Text(
                        text = stringResource(Res.string.create) + ": ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = sale.createDate)
                }
            }
        }
        Row(modifier = Modifier.padding(16.dp)) {
            Button(
                modifier = Modifier.weight(1F),
                onClick = { onUpdate(sale) },
                content = {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = ""
                        )
                        Text(text = stringResource(Res.string.update))
                    }
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(
                modifier = Modifier.weight(1F),
                onClick = { onDelete() },
                content = {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = ""
                        )
                        Text(text = stringResource(Res.string.delete))
                    }
                }
            )
        }
        val headersSaleLines = listOf(
            "#",
            stringResource(Res.string.name),
            stringResource(Res.string.stock),
            stringResource(Res.string.single_price),
            stringResource(Res.string.total_price)
        )

        val saleLinesColumnContents: List<@Composable (index: Int, item: SaleLine) -> Unit> =
            listOf(
                { index, _ ->
                    Text(
                        text = (index + 1).toString(),
                        textAlign = TextAlign.Center
                    )
                },
                { _, item ->
                    Text(
                        text = item.productName,
                        textAlign = TextAlign.Center
                    )
                },
                { _, item ->
                    Text(
                        text = item.productStock.toStringRounded(),
                        textAlign = TextAlign.Center
                    )
                },
                { _, item ->
                    Text(
                        text = item.singlePrice.toStringRounded(),
                        textAlign = TextAlign.Center
                    )
                },
                { _, item ->
                    Text(
                        text = item.totalPrice.toStringRounded(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        SimpleTable(
            headers = headersSaleLines,
            data = sale.lines,
            columnContents = saleLinesColumnContents,
            onDeleteItem = { },
            showDeleteButton = false,
            showTotalRow = true,
            totalRowContent = {
                Text(
                    text = "${stringResource(Res.string.total)} ${
                        sale.lines.sumOf { it.totalPrice }.toStringRounded()
                    }",
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        )
    }
}

@Composable
fun FromSale(viewModel: SaleViewModel, state: SaleState) {
    val (item1, item2) = remember { FocusRequester.createRefs() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val isQuantityTouched = remember { mutableStateOf(false) }
    val isPriceTouched = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(end = 50.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(
                onClick = { viewModel.onEvent(SaleEvent.OnDismissSale) },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.error),
                    contentDescription = stringResource(Res.string.close)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (state.sale.id == 0) {
                stringResource(Res.string.create_sale)
            } else {
                stringResource(Res.string.update_sale)
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            modifier = Modifier.height(ButtonDefaults.MinHeight)
                .width(OutlinedTextFieldDefaults.MinWidth),
            onClick = { viewModel.onEvent(SaleEvent.OnChangeClient) }
        ) {
            Text(
                text = state.client.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = " (${state.client.identityCard.formatNumberInt()})",
                fontSize = 12.sp,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth))
        Row(
            modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AutoCompleteGeneric(
                modifier = Modifier.weight(1f),
                items = state.products,
                itemToString = { product -> product.name },
                label = stringResource(Res.string.select_product),
                placeholder = "",
                onItemSelected = { viewModel.onEvent(SaleEvent.OnSelectedProduct(it)) },
                searchText = state.searchProduct,
                onValueChange = { viewModel.onEvent(SaleEvent.OnSearchItem(it)) }
            )
            IconButton(
                onClick = { viewModel.onEvent(SaleEvent.OnDisplaySaleLines) },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.show),
                        contentDescription = stringResource(Res.string.show)
                    )
                }
            )
        }
        Row(
            modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.productQuantity,
                onValueChange = { viewModel.onEvent(SaleEvent.OnChanceQuantityProduct(it)) },
                label = { Text(text = stringResource(Res.string.quantity)) },
                singleLine = true,
                modifier = Modifier.weight(1f).padding(end = 4.dp)
                    .focusRequester(item1)
                    .focusProperties { next = item2 }
                    .onFocusChanged {
                        if (it.isFocused) {
                            isQuantityTouched.value = true
                        } else if (isQuantityTouched.value) {
                            viewModel.onEvent(SaleEvent.OnValidateQuantityProduct)
                        }
                    },
                isError = isQuantityTouched.value && state.errorQuantity != null,
                supportingText = {
                    if (isQuantityTouched.value) {
                        state.errorQuantity?.let { errorRes ->
                            Text(text = stringResource(errorRes))
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { item2.requestFocus() })
            )
            OutlinedTextField(
                value = state.productPrice,
                onValueChange = {},
                label = { Text(text = stringResource(Res.string.price)) },
                singleLine = true,
                modifier = Modifier.weight(1f).padding(start = 4.dp),
                enabled = false,
                isError = false,
                supportingText = { Text(text = "") }
            )
            IconButton(
                onClick = { viewModel.onEvent(SaleEvent.OnAddList) },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.add),
                        contentDescription = stringResource(Res.string.add)
                    )
                },
                enabled = state.product.id != 0 && state.errorQuantity == null
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth))
        Row(
            modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.sale.totalPrice,
                onValueChange = { viewModel.onEvent(SaleEvent.OnPriceChance(it)) },
                label = {
                    Text(text = stringResource(Res.string.price))
                },
                singleLine = true,
                modifier = Modifier.weight(1f)
                    .focusRequester(item2)
                    .focusProperties { previous = item1 }
                    .onFocusChanged {
                        if (it.isFocused) {
                            isPriceTouched.value = true
                        } else if (isPriceTouched.value) {
                            viewModel.onEvent(SaleEvent.OnValidatePriceSale)
                        }
                    },
                enabled = state.enableEditPrice,
                isError = isPriceTouched.value && state.errorPrice != null,
                supportingText = {
                    if (isPriceTouched.value) {
                        state.errorPrice?.let { errorRes ->
                            Text(text = stringResource(errorRes))
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    if (state.sale.id == 0) {
                        viewModel.onEvent(SaleEvent.OnSumit)
                    } else {
                        viewModel.onEvent(SaleEvent.OnUpdate(state.sale))
                    }
                })
            )
            Checkbox(
                checked = state.enableEditPrice,
                onCheckedChange = { viewModel.onEvent(SaleEvent.OnChangeEnablePrice(it)) }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth))
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier.height(ButtonDefaults.MinHeight)
                .width(OutlinedTextFieldDefaults.MinWidth),
            onClick = {
                if (state.sale.id == 0) {
                    viewModel.onEvent(SaleEvent.OnSumit)
                } else {
                    viewModel.onEvent(SaleEvent.OnUpdate(state.sale))
                }
            },
            enabled = !state.inputErrorSale && state.inputsFillSale,
            content = {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                    }

                    state.isError -> {
                        Icon(
                            painter = painterResource(Res.drawable.error),
                            contentDescription = stringResource(Res.string.error),
                        )
                    }

                    state.isSuccess -> {
                        Icon(
                            painter = painterResource(Res.drawable.success),
                            contentDescription = stringResource(Res.string.success),
                        )
                    }

                    else -> {
                        if (state.sale.id == 0) {
                            Text(text = stringResource(Res.string.create))
                        } else {
                            Text(text = stringResource(Res.string.update))
                        }
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FromClient(viewModel: SaleViewModel, state: SaleState) {
    val (item1, item2) = remember { FocusRequester.createRefs() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val isIdentityCardTouched = remember { mutableStateOf(false) }
    val isNameTouched = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(end = 50.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(
                onClick = { viewModel.onEvent(SaleEvent.OnDismissAddClient) },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.error),
                    contentDescription = stringResource(Res.string.close)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (state.client.id == 0) {
                stringResource(Res.string.create_client)
            } else {
                stringResource(Res.string.update_client)
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.client.identityCard,
            onValueChange = { viewModel.onEvent(SaleEvent.OnIdentityCardClientChance(it)) },
            label = {
                Text(text = stringResource(Res.string.identity_card))
            },
            isError = isIdentityCardTouched.value && state.errorIdentityCard != null,
            supportingText = {
                if (isIdentityCardTouched.value) {
                    state.errorIdentityCard?.let { errorRes ->
                        Text(text = stringResource(errorRes))
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .focusRequester(item1)
                .focusProperties { next = item2 }
                .onFocusChanged {
                    if (it.isFocused) {
                        isIdentityCardTouched.value = true
                    } else if (isIdentityCardTouched.value) {
                        viewModel.onEvent(SaleEvent.OnValidateIdentityCardClient)
                    }
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { item2.requestFocus() })
        )
        OutlinedTextField(
            value = state.client.name,
            onValueChange = { viewModel.onEvent(SaleEvent.OnNameCLientChance(it)) },
            label = {
                Text(text = stringResource(Res.string.name))
            },
            isError = isNameTouched.value && state.errorName != null,
            supportingText = {
                if (isNameTouched.value) {
                    state.errorName?.let { errorRes ->
                        Text(text = stringResource(errorRes))
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .focusRequester(item2)
                .focusProperties { previous = item1 }
                .onFocusChanged {
                    if (it.isFocused) {
                        isNameTouched.value = true
                    } else if (isNameTouched.value) {
                        viewModel.onEvent(SaleEvent.OnValidateNameClient)
                    }
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
                if (state.client.id == 0) {
                    viewModel.onEvent(SaleEvent.OnSumitClient)
                } else {
                    viewModel.onEvent(SaleEvent.OnUpdateClient(state.client))
                }
            })
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth))
        Button(
            modifier = Modifier.height(ButtonDefaults.MinHeight)
                .width(OutlinedTextFieldDefaults.MinWidth),
            onClick = {
                if (state.client.id == 0) {
                    viewModel.onEvent(SaleEvent.OnSumitClient)
                } else {
                    viewModel.onEvent(SaleEvent.OnUpdateClient(state.client))
                }
            },
            enabled = !state.inputError && state.inputsFill,
            content = {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                    }

                    state.isError -> {
                        Icon(
                            painter = painterResource(Res.drawable.error),
                            contentDescription = stringResource(Res.string.error),
                        )
                    }

                    state.isSuccess -> {
                        Icon(
                            painter = painterResource(Res.drawable.success),
                            contentDescription = stringResource(Res.string.success),
                        )
                    }

                    else -> {
                        if (state.client.id == 0) {
                            Text(text = stringResource(Res.string.create))
                        } else {
                            Text(text = stringResource(Res.string.update))
                        }
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}