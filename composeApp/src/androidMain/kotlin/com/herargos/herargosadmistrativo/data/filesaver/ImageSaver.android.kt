package com.herargos.herargosadmistrativo.data.filesaver

import android.content.Context
import java.io.File
import java.io.FileOutputStream

/*actual fun getImageSaver(): ImageSaver {
    return AndroidImageSaver()
}*/

actual class ImageSaverImpl : ImageSaver {

    private lateinit var context: Context

    fun initialize(context: Context) {
        this.context = context
    }

    override fun saveImage(imageData: ByteArray, fileName: String): String {
        val file = File(context.filesDir, fileName)
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