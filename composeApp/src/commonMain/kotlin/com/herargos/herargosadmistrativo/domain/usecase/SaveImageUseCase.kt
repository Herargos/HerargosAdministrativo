package com.herargos.herargosadmistrativo.domain.usecase

import com.herargos.herargosadmistrativo.core.utils.isJpeg
import com.herargos.herargosadmistrativo.core.utils.isPng
import com.herargos.herargosadmistrativo.core.utils.isWebp
import com.herargos.herargosadmistrativo.data.filesaver.ImageSaver
import java.util.UUID

class SaveImageUseCase(private val imageSaver: ImageSaver) {
    operator fun invoke(imageData: ByteArray): String? { // <- Ahora puede devolver null
        val fileExtension = getImageExtension(imageData)

        // Si no se pudo detectar la extensión, retorna null
        if (fileExtension == null) {
            return null
        }

        val fileName = "${UUID.randomUUID()}.$fileExtension"
        return imageSaver.saveImage(imageData, fileName)
    }

    private fun getImageExtension(imageData: ByteArray): String? {
        return when {
            imageData.isJpeg() -> "jpg"
            imageData.isPng() -> "png"
            imageData.isWebp() -> "webp"
            else -> null // <- Retorna null si no se reconoce el formato
        }
    }
}