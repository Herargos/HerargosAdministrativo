package com.herargos.herargosadmistrativo.data.filesaver

import com.herargos.herargosadmistrativo.data.getAppFolder
import java.io.File
import java.io.FileOutputStream

/*actual fun getImageSaver(): ImageSaver {
    // Aquí pasas la ruta de la aplicación a la implementación.
    return DesktopImageSaver(getAppFolder())
}*/

actual class ImageSaverImpl : ImageSaver {
    override fun saveImage(imageData: ByteArray, fileName: String): String {
        val file = File(getAppFolder(), fileName)
        FileOutputStream(file).use { outputStream ->
            outputStream.write(imageData)
        }
        return file.absolutePath
    }

    override fun deleteImage(filePath: String): Boolean {
        return try {
            val file = File(filePath)
            file.delete()
        } catch (e: Exception) {
            false
        }
    }
}