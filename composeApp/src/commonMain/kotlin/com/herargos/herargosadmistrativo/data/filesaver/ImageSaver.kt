package com.herargos.herargosadmistrativo.data.filesaver

interface ImageSaver {
    fun saveImage(imageData: ByteArray, fileName: String): String
    fun deleteImage(filePath: String): Boolean
}

expect class ImageSaverImpl(): ImageSaver
