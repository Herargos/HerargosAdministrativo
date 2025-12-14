package com.herargos.herargosadmistrativo.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ImagePickerLauncher(
    modifier: Modifier = Modifier,
    selectedImage: ByteArray?,
    dbImage: String?,
    onImagePicked: (ByteArray?) -> Unit
)