package com.herargos.herargosadmistrativo.ui.ingredient.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
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
import com.herargos.herargosadmistrativo.core.ui.ImagePickerLauncher
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.add
import herargosadministrativo.composeapp.generated.resources.arrow_back
import herargosadministrativo.composeapp.generated.resources.close
import herargosadministrativo.composeapp.generated.resources.create
import herargosadministrativo.composeapp.generated.resources.create_ingredient
import herargosadministrativo.composeapp.generated.resources.delete
import herargosadministrativo.composeapp.generated.resources.delete_item
import herargosadministrativo.composeapp.generated.resources.delete_item_description
import herargosadministrativo.composeapp.generated.resources.error
import herargosadministrativo.composeapp.generated.resources.image_placeholder
import herargosadministrativo.composeapp.generated.resources.img
import herargosadministrativo.composeapp.generated.resources.name
import herargosadministrativo.composeapp.generated.resources.price
import herargosadministrativo.composeapp.generated.resources.show_name
import herargosadministrativo.composeapp.generated.resources.show_price
import herargosadministrativo.composeapp.generated.resources.show_stock
import herargosadministrativo.composeapp.generated.resources.show_unit
import herargosadministrativo.composeapp.generated.resources.stock
import herargosadministrativo.composeapp.generated.resources.success
import herargosadministrativo.composeapp.generated.resources.unit
import herargosadministrativo.composeapp.generated.resources.update
import herargosadministrativo.composeapp.generated.resources.update_ingredient
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IngredientScreen(
    onBack: () -> Unit,
    viewModel: IngredientViewModel = koinViewModel(parameters = { parametersOf(onBack) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box {
                IconButton(
                    onClick = { viewModel.onEvent(IngredientEvents.OnBack) },
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
                    viewModel.onEvent(IngredientEvents.OnDisplay(null))
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
                    onDismissRequest = { viewModel.onEvent(IngredientEvents.OnDismiss) },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Card(
                        modifier = Modifier
                            .width(700.dp)
                            .height(600.dp)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Form(state, viewModel)
                    }
                }
            }
            AnimatedVisibility(state.showDialogDelete) {
                AlertDialogExample(
                    onDismissRequest = { viewModel.onEvent(IngredientEvents.OnDismissDelete) },
                    onConfirmation = { viewModel.onEvent(IngredientEvents.OnConfirmDelete(state.ingredientSelected)) },
                    dialogTitle = stringResource(Res.string.delete_item),
                    dialogText = stringResource(Res.string.delete_item_description) + state.ingredientSelected.name,
                    icon = Res.drawable.delete
                )
            }
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                columns = GridCells.Adaptive(minSize = 190.dp),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.ingredients) { ingredient ->
                    ElevatedCard(
                        Modifier.height(256.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                                .combinedClickable(
                                    onDoubleClick = {
                                        viewModel.onEvent(
                                            IngredientEvents.OnDisplay(
                                                ingredient
                                            )
                                        )
                                    },
                                    onLongClick = {
                                        viewModel.onEvent(
                                            IngredientEvents.OnDisplayDelete(
                                                ingredient
                                            )
                                        )
                                    },
                                    onClick = {}
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                if (ingredient.imagePath != null) {
                                    // Si existe, usamos AsyncImage de Coil
                                    AsyncImage(
                                        model = ingredient.imagePath,
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
                                        text = ingredient.name
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
                                        Text(text = ingredient.stock)
                                        Icon(
                                            imageVector = Icons.Rounded.Warning,
                                            contentDescription = "",
                                            tint = when {
                                                (ingredient.stock.toDouble() <= 5) -> {
                                                    Color.Red.copy(alpha = 0.55f)
                                                }

                                                (ingredient.stock.toDouble() <= 10) -> {
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
                                        Text(text = ingredient.price)
                                        Text(text = stringResource(Res.string.show_unit))
                                        Text(text = ingredient.unit)
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
fun Form(state: IngredientState, viewModel: IngredientViewModel) {
    val (item1, item2, item3, item4) = remember { FocusRequester.createRefs() }
    val focusManager = LocalFocusManager.current
    val isNameTouched = remember { mutableStateOf(false) }
    val isStockTouched = remember { mutableStateOf(false) }
    val isUnitTouched = remember { mutableStateOf(false) }
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
                onClick = { viewModel.onEvent(IngredientEvents.OnDismiss) },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.error),
                    contentDescription = stringResource(Res.string.close)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (state.ingredient.id == 0) {
                stringResource(Res.string.create_ingredient)
            } else {
                stringResource(Res.string.update_ingredient)
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
                    dbImage = state.ingredient.imagePath,
                ) { imageData ->
                    if (imageData != null) {
                        viewModel.onEvent(IngredientEvents.OnImageChanged(imageData))
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
                    value = state.ingredient.name,
                    onValueChange = { viewModel.onEvent(IngredientEvents.OnNameChanged(it)) },
                    label = { Text(text = stringResource(Res.string.name)) },
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
                                viewModel.onEvent(IngredientEvents.OnValidateName)
                            }
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { item2.requestFocus() })
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.ingredient.stock,
                    onValueChange = { viewModel.onEvent(IngredientEvents.OnStockChanged(it)) },
                    label = { Text(text = stringResource(Res.string.stock)) },
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
                                viewModel.onEvent(IngredientEvents.OnValidateStock)
                            }
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { item3.requestFocus() })
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.ingredient.unit,
                    onValueChange = { viewModel.onEvent(IngredientEvents.OnUnitChanged(it)) },
                    label = { Text(text = stringResource(Res.string.unit)) },
                    isError = isUnitTouched.value && state.errorUnit != null,
                    supportingText = {
                        if (isUnitTouched.value) {
                            state.errorUnit?.let { errorRes ->
                                Text(text = stringResource(errorRes))
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .focusRequester(item3)
                        .focusProperties { next = item4; previous = item2 }
                        .onFocusChanged {
                            if (it.isFocused) {
                                isUnitTouched.value = true
                            } else if (isUnitTouched.value) {
                                viewModel.onEvent(IngredientEvents.OnValidateUnit)
                            }
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { item4.requestFocus() })
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.ingredient.price,
                    onValueChange = { viewModel.onEvent(IngredientEvents.OnPriceChanged(it)) },
                    label = { Text(text = stringResource(Res.string.price)) },
                    isError = isPriceTouched.value && state.errorPrice != null,
                    supportingText = {
                        if (isPriceTouched.value) {
                            state.errorPrice?.let { errorRes ->
                                Text(text = stringResource(errorRes))
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .focusRequester(item4)
                        .focusProperties { previous = item3 }
                        .onFocusChanged {
                            if (it.isFocused) {
                                isPriceTouched.value = true
                            } else if (isPriceTouched.value) {
                                viewModel.onEvent(IngredientEvents.OnValidatePrice)
                            }
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (!state.inputError && state.inputsFill) {
                                if (state.ingredient.id == 0) {
                                    viewModel.onEvent(IngredientEvents.OnSubmitIngredient)
                                } else {
                                    viewModel.onEvent(IngredientEvents.OnUpdateIngredient(state.ingredient))
                                }
                            }
                            focusManager.clearFocus()
                        }
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth))
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier.height(ButtonDefaults.MinHeight)
                .width(OutlinedTextFieldDefaults.MinWidth),
            enabled = !state.inputError && state.inputsFill,
            onClick = {
                if (state.ingredient.id == 0) {
                    viewModel.onEvent(IngredientEvents.OnSubmitIngredient)
                } else {
                    viewModel.onEvent(IngredientEvents.OnUpdateIngredient(state.ingredient))
                }
            },
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
                        if (state.ingredient.id == 0) {
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