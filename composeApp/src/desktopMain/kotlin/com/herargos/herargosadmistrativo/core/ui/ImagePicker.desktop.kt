package com.herargos.herargosadmistrativo.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.img
import herargosadministrativo.composeapp.generated.resources.select_image
import org.jetbrains.compose.resources.stringResource
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun ImagePickerLauncher(
    modifier: Modifier,
    selectedImage: ByteArray?,
    dbImage: String?,
    onImagePicked: (ByteArray?) -> Unit
) {
    val imageBitmap = remember(selectedImage) {
        selectedImage?.let { org.jetbrains.skia.Image.makeFromEncoded(it).toComposeImageBitmap() }
    }
    val text = stringResource(Res.string.select_image)
    OutlinedButton(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        onClick = {
            // Mueve la lógica a un hilo secundario para evitar el bloqueo de la UI
            Thread {
                val fileDialog = FileDialog(Frame(), text, FileDialog.LOAD)
                fileDialog.setFilenameFilter { dir, name ->
                    name.endsWith(".jpg", ignoreCase = true) ||
                            name.endsWith(".png", ignoreCase = true) ||
                            name.endsWith(".webp", ignoreCase = true)
                }
                fileDialog.isVisible = true

                val selectedFile: String? = fileDialog.file
                val selectedDirectory: String? = fileDialog.directory

                if (selectedFile != null && selectedDirectory != null) {
                    val fullPath = File(selectedDirectory, selectedFile).absolutePath
                    val file = File(fullPath)
                    if (file.exists()) {
                        val imageData = file.readBytes()
                        onImagePicked(imageData)
                    }
                } else {
                    onImagePicked(null)
                }
            }.start()
        },
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                imageBitmap != null -> {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                dbImage != null -> {
                    AsyncImage(
                        model = dbImage,
                        contentDescription = stringResource(Res.string.img),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    Text(text = text)
                }
            }
        }
    }
}