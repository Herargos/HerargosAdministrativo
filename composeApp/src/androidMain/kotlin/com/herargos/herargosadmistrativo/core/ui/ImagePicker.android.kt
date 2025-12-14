package com.herargos.herargosadmistrativo.core.ui

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.select_image
import org.jetbrains.compose.resources.stringResource


// La función 'expect' se resuelve aquí
@Composable
actual fun ImagePickerLauncher(
    modifier: Modifier,
    selectedImage: ByteArray?,
    dbImage: (ByteArray?) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val imageData = context.contentResolver.openInputStream(uri)?.readBytes()
            dbImage(imageData)
        } else {
            dbImage(null)
        }
    }

    // Usamos remember para evitar recalcular la imagen en cada recomposición
    val imageBitmap = remember(selectedImage) {
        selectedImage?.let { BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap() }
    }

    OutlinedButton(
        modifier = modifier,
        onClick = { launcher.launch("image/*") }
    ) {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(text = stringResource(Res.string.select_image))
        }
    }
}