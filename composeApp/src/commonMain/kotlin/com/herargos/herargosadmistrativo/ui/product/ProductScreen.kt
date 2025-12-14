package com.herargos.herargosadmistrativo.ui.product

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.herargos.herargosadmistrativo.core.ui.AlertDialogExample
import com.herargos.herargosadmistrativo.core.ui.AutoCompleteGeneric
import com.herargos.herargosadmistrativo.core.ui.ImagePickerLauncher
import com.herargos.herargosadmistrativo.core.ui.SimpleTable
import com.herargos.herargosadmistrativo.core.utils.toStringRounded
import com.herargos.herargosadmistrativo.domain.model.Recipe
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.add
import herargosadministrativo.composeapp.generated.resources.arrow_back
import herargosadministrativo.composeapp.generated.resources.close
import herargosadministrativo.composeapp.generated.resources.create
import herargosadministrativo.composeapp.generated.resources.create_product
import herargosadministrativo.composeapp.generated.resources.delete
import herargosadministrativo.composeapp.generated.resources.delete_item
import herargosadministrativo.composeapp.generated.resources.delete_item_description
import herargosadministrativo.composeapp.generated.resources.error
import herargosadministrativo.composeapp.generated.resources.image_placeholder
import herargosadministrativo.composeapp.generated.resources.img
import herargosadministrativo.composeapp.generated.resources.name
import herargosadministrativo.composeapp.generated.resources.price
import herargosadministrativo.composeapp.generated.resources.quantity
import herargosadministrativo.composeapp.generated.resources.ref
import herargosadministrativo.composeapp.generated.resources.select_product
import herargosadministrativo.composeapp.generated.resources.show
import herargosadministrativo.composeapp.generated.resources.show_name
import herargosadministrativo.composeapp.generated.resources.show_price
import herargosadministrativo.composeapp.generated.resources.show_stock
import herargosadministrativo.composeapp.generated.resources.stock
import herargosadministrativo.composeapp.generated.resources.success
import herargosadministrativo.composeapp.generated.resources.total
import herargosadministrativo.composeapp.generated.resources.update
import herargosadministrativo.composeapp.generated.resources.update_product
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductScreen(
    onBack: () -> Unit,
    viewModel: ProductViewModel = koinViewModel(parameters = { parametersOf(onBack) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()
    val snackbarHostState = remember { SnackbarHostState() }

    state.textSnackBar?.let {
        val message = stringResource(it)
        LaunchedEffect(key1 = message) {
            snackbarHostState.showSnackbar(
                message = message
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Box {
                IconButton(
                    onClick = { viewModel.onEvent(ProductEvents.OnBack) },
                    content = {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_back),
                            contentDescription = stringResource(Res.string.arrow_back)
                        )
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(ProductEvents.OnDisplay(null))
                },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.add),
                        contentDescription = stringResource(Res.string.add)
                    )
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            AnimatedVisibility(state.showDialog) {
                Dialog(
                    onDismissRequest = { viewModel.onEvent(ProductEvents.OnDismiss) },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Card(
                        modifier = Modifier
                            .width(700.dp)
                            .height(700.dp)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Form(state, viewModel)
                    }
                }
            }
            AnimatedVisibility(state.showDialogList) {
                Dialog(onDismissRequest = { viewModel.onEvent(ProductEvents.OnDismissList) }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(600.dp)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        val headersRecipe = listOf(
                            "#",
                            stringResource(Res.string.name),
                            stringResource(Res.string.quantity),
                            stringResource(Res.string.price),
                            "" // Para el botón de eliminar
                        )

                        val recipeColumnContents: List<@Composable (index: Int, item: Recipe) -> Unit> =
                            listOf(
                                { index, _ -> // Solo usamos el índice aquí, el item no es necesario para el número de fila
                                    Text(
                                        text = (index + 1).toString(), // ¡Usamos el índice directamente!
                                        textAlign = TextAlign.Center
                                    )
                                },
                                { _, item -> // Aquí el índice no se usa, pero se recibe
                                    Text(
                                        text = item.name,
                                        textAlign = TextAlign.Center
                                    )
                                },
                                { _, item ->
                                    Text(
                                        text = item.quantity.toStringRounded(),
                                        textAlign = TextAlign.Center
                                    )
                                },
                                { _, item ->
                                    Text(
                                        text = item.price.toStringRounded(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )

                        SimpleTable(
                            headers = headersRecipe,
                            data = state.recipe,
                            columnContents = recipeColumnContents,
                            onDeleteItem = { item ->
                                viewModel.onEvent(
                                    ProductEvents.OnDeleteItemList(
                                        item
                                    )
                                )
                            },
                            showDeleteButton = true,
                            showTotalRow = true,
                            totalRowContent = {
                                Text(
                                    text = "${stringResource(Res.string.total)} ${
                                        state.recipe.sumOf { it.price }.toStringRounded()
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
            AnimatedVisibility(state.showDialogDelete) {
                AlertDialogExample(
                    onDismissRequest = { viewModel.onEvent(ProductEvents.OnDismissDelete) },
                    onConfirmation = { viewModel.onEvent(ProductEvents.OnConfirmDelete(state.productSelected)) },
                    dialogTitle = stringResource(Res.string.delete_item),
                    dialogText = stringResource(Res.string.delete_item_description) + state.productSelected.name,
                    icon = Res.drawable.delete
                )
            }
            AnimatedVisibility(state.showDialogDetail) {
                Dialog(onDismissRequest = { viewModel.onEvent(ProductEvents.OnDismissDetail) }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(600.dp)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        CardDetail(state, viewModel)
                    }
                }
            }
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                columns = GridCells.Adaptive(minSize = 190.dp),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.products) { product ->
                    ElevatedCard(
                        Modifier.height(256.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                                .combinedClickable(
                                    onDoubleClick = {
                                        viewModel.onEvent(
                                            ProductEvents.OnDisplay(
                                                product
                                            )
                                        )
                                    },
                                    onLongClick = {
                                        viewModel.onEvent(
                                            ProductEvents.OnDisplayDelete(
                                                product
                                            )
                                        )
                                    },
                                    onClick = {
                                        viewModel.onEvent(
                                            ProductEvents.OnDisplayDetail(
                                                product
                                            )
                                        )
                                    }
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                if (product.imagePath != null) {
                                    // Si existe, usamos AsyncImage de Coil
                                    AsyncImage(
                                        model = product.imagePath,
                                        contentDescription = stringResource(Res.string.img),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    // Si no hay ruta, mostramos el icono de placeholder
                                    Icon(
                                        modifier = Modifier.fillMaxSize(),
                                        painter = painterResource(Res.drawable.image_placeholder),
                                        contentDescription = stringResource(Res.string.img)
                                    )
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row {
                                    Text(
                                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                                        text = stringResource(Res.string.show_name),
                                        textAlign = TextAlign.End,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                                        text = product.name
                                    )

                                }
                                Row {
                                    Text(
                                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                                        text = stringResource(Res.string.show_stock),
                                        textAlign = TextAlign.End,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                    )
                                    Row(modifier = Modifier.weight(1f).padding(horizontal = 2.dp)) {
                                        Text(text = product.stock)
                                        Icon(
                                            imageVector = Icons.Rounded.Warning,
                                            contentDescription = "",
                                            tint = when {
                                                (product.stock.toDouble() <= 5) -> {
                                                    Color.Red.copy(alpha = 0.55f)
                                                }

                                                (product.stock.toDouble() <= 10) -> {
                                                    Color.Yellow.copy(alpha = 0.55f)
                                                }

                                                else -> {
                                                    Color.Transparent
                                                }
                                            }
                                        )
                                    }
                                }
                                Row {
                                    Text(
                                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                                        text = stringResource(Res.string.show_price),
                                        textAlign = TextAlign.End,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                    )
                                    Row(modifier = Modifier.weight(1f).padding(horizontal = 2.dp)) {
                                        Text(text = product.price)
                                        Text(text = stringResource(Res.string.ref))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardDetail(state: ProductState, viewModel: ProductViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                modifier = Modifier.size(250.dp),
                painter = painterResource(Res.drawable.image_placeholder),
                contentDescription = stringResource(Res.string.img)
            )
            Column(modifier = Modifier.weight(1f).padding(top = 24.dp)) {
                Row {
                    Text(text = stringResource(Res.string.show_name))
                    Text(text = state.productSelected.name)
                }
                Row {
                    Text(text = stringResource(Res.string.show_price))
                    Text(text = state.productSelected.price)
                }
                Row {
                    Text(text = stringResource(Res.string.show_stock))
                    Text(text = state.productSelected.stock + stringResource(Res.string.ref))
                }
                Button(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    onClick = { viewModel.onEvent(ProductEvents.OnDisplay(state.productSelected)) },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(Res.string.update)
                        )
                        Text(text = stringResource(Res.string.update))
                    }
                )
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    onClick = { viewModel.onEvent(ProductEvents.OnDisplayDelete(state.productSelected)) },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(Res.string.delete)
                        )
                        Text(text = stringResource(Res.string.delete))
                    }
                )
            }
            IconButton(
                onClick = { viewModel.onEvent(ProductEvents.OnDismissDetail) },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.error),
                    contentDescription = stringResource(Res.string.close)
                )
            }
        }
        val headersRecipeNoDelete = listOf(
            "#",
            stringResource(Res.string.name),
            stringResource(Res.string.quantity),
            stringResource(Res.string.price)
        )

        val recipeNoDeleteColumnContents: List<@Composable (index: Int, item: Recipe) -> Unit> =
            listOf(
                { index, _ ->
                    Text(
                        text = (index + 1).toString(),
                        textAlign = TextAlign.Center
                    )
                },
                { _, item ->
                    Text(
                        text = item.name,
                        textAlign = TextAlign.Center
                    )
                },
                { _, item ->
                    Text(
                        text = item.quantity.toStringRounded(),
                        textAlign = TextAlign.Center
                    )
                },
                { _, item ->
                    Text(
                        text = item.price.toStringRounded(),
                        textAlign = TextAlign.Center
                    )
                }
            )

        SimpleTable(
            headers = headersRecipeNoDelete,
            data = state.productSelected.recipes,
            columnContents = recipeNoDeleteColumnContents,
            showDeleteButton = false,
            showTotalRow = false
        )
    }
}

@Composable
fun Form(state: ProductState, viewModel: ProductViewModel) {
    val (item1, item2, item3, item4) = remember { FocusRequester.createRefs() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Estados para rastrear si cada campo ha sido tocado
    val isNameTouched = remember { mutableStateOf(false) }
    val isStockTouched = remember { mutableStateOf(false) }
    val isPriceTouched = remember { mutableStateOf(false) }
    val isQuantityTouched = remember { mutableStateOf(false) }

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
                onClick = { viewModel.onEvent(ProductEvents.OnDismiss) },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.error),
                    contentDescription = stringResource(Res.string.close)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (state.product.id == 0) {
                stringResource(Res.string.create_product)
            } else {
                stringResource(Res.string.update_product)
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.weight(1F), contentAlignment = Alignment.Center) {
                ImagePickerLauncher(
                    modifier = Modifier
                        .height(OutlinedTextFieldDefaults.MinWidth)
                        .width(OutlinedTextFieldDefaults.MinWidth),
                    selectedImage = state.imageData,
                    dbImage = state.product.imagePath,
                ) { imageData ->
                    if (imageData != null) {
                        viewModel.onEvent(ProductEvents.OnImageChanged(imageData))
                    }
                }
            }
            VerticalDivider(modifier = Modifier.height(OutlinedTextFieldDefaults.MinWidth))
            Column(
                modifier = Modifier.weight(1F),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = state.product.name,
                    onValueChange = { viewModel.onEvent(ProductEvents.OnNameChance(it)) },
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
                        .focusRequester(item1)
                        .focusProperties { next = item2 }
                        .onFocusChanged {
                            if (it.isFocused) {
                                isNameTouched.value = true
                            } else if (isNameTouched.value) {
                                viewModel.onEvent(ProductEvents.OnValidateName)
                            }
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { item2.requestFocus() })
                )
                OutlinedTextField(
                    value = state.product.stock,
                    onValueChange = { viewModel.onEvent(ProductEvents.OnStockChance(it)) },
                    label = {
                        Text(text = stringResource(Res.string.stock))
                    },
                    isError = isStockTouched.value && state.errorStock != null,
                    supportingText = {
                        if (isStockTouched.value) {
                            state.errorStock?.let { errorRes ->
                                Text(text = stringResource(errorRes))
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .focusRequester(item2)
                        .focusProperties { next = item3; previous = item1 }
                        .onFocusChanged {
                            if (it.isFocused) {
                                isStockTouched.value = true
                            } else if (isStockTouched.value) {
                                viewModel.onEvent(ProductEvents.OnValidateStock)
                            }
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { item3.requestFocus() })
                )
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth))
                Row(
                    modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AutoCompleteGeneric(
                        modifier = Modifier.weight(1f),
                        items = state.ingredientsDB,
                        itemToString = { ingredient -> ingredient.name },
                        label = stringResource(Res.string.select_product),
                        placeholder = "",
                        onItemSelected = { viewModel.onEvent(ProductEvents.OnSelectedIngredient(it)) },
                        searchText = state.searchText,
                        onValueChange = { viewModel.onEvent(ProductEvents.OnSearchItem(it)) }
                    )
                    IconButton(
                        onClick = { viewModel.onEvent(ProductEvents.OnDisplayList) },
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
                        value = state.ingredientQuantity,
                        onValueChange = {
                            viewModel.onEvent(
                                ProductEvents.OnChanceQuantityIngredient(
                                    it
                                )
                            )
                        },
                        label = { Text(text = stringResource(Res.string.quantity)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                            .focusRequester(item3)
                            .focusProperties { next = item4; previous = item2 }
                            .onFocusChanged {
                                if (it.isFocused) {
                                    isQuantityTouched.value = true
                                } else if (isQuantityTouched.value) {
                                    viewModel.onEvent(ProductEvents.OnValidateQuantity)
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
                        keyboardActions = KeyboardActions(onNext = { item4.requestFocus() }),
                        enabled = state.enableQuantity
                    )
                    OutlinedTextField(
                        value = state.ingredientPrice,
                        onValueChange = {},
                        label = {
                            Text(text = stringResource(Res.string.price))
                        },
                        singleLine = true,
                        modifier = Modifier.weight(1f).padding(start = 4.dp),
                        enabled = false,
                        isError = false,
                        supportingText = { Text(text = "") }
                    )
                    IconButton(
                        onClick = { viewModel.onEvent(ProductEvents.OnAddList) },
                        content = {
                            Icon(
                                painter = painterResource(Res.drawable.add),
                                contentDescription = stringResource(Res.string.add)
                            )
                        },
                        enabled = state.ingredientSelected.id != 0 && state.errorQuantity == null
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth))
                Row(
                    modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.product.price,
                        onValueChange = { viewModel.onEvent(ProductEvents.OnPriceChance(it)) },
                        label = {
                            Text(text = stringResource(Res.string.price))
                        },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                            .focusRequester(item4)
                            .focusProperties { previous = item3 }
                            .onFocusChanged {
                                if (it.isFocused) {
                                    isPriceTouched.value = true
                                } else if (isPriceTouched.value) {
                                    viewModel.onEvent(ProductEvents.OnValidatePrice)
                                }
                            },
                        enabled = state.enablePrice,
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
                            if (state.product.id == 0) {
                                viewModel.onEvent(ProductEvents.OnSubmit)
                            } else {
                                viewModel.onEvent(ProductEvents.OnUpdate)
                            }
                        })
                    )
                    Checkbox(
                        checked = state.enablePrice,
                        onCheckedChange = { viewModel.onEvent(ProductEvents.OnChangeEnablePrice(it)) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth))
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier.height(ButtonDefaults.MinHeight)
                .width(OutlinedTextFieldDefaults.MinWidth),
            onClick = {
                if (state.product.id == 0) {
                    viewModel.onEvent(ProductEvents.OnSubmit)
                } else {
                    viewModel.onEvent(ProductEvents.OnUpdate)
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
                        if (state.product.id == 0) {
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