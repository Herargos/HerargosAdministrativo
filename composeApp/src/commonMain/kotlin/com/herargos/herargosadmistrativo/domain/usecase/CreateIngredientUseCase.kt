package com.herargos.herargosadmistrativo.domain.usecase

import co.touchlab.kermit.Logger
import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.core.ResponseMessage
import com.herargos.herargosadmistrativo.data.filesaver.ImageSaver
import com.herargos.herargosadmistrativo.domain.model.Ingredient
import com.herargos.herargosadmistrativo.domain.repository.IngredientRepository
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.unknown_error_create_ingredient
import herargosadministrativo.composeapp.generated.resources.unsupported_image_format

class CreateIngredientUseCase(
    private val ingredientRepository: IngredientRepository,
    private val saveImageUseCase: SaveImageUseCase,
    private val imageSaver: ImageSaver
) {
    suspend operator fun invoke(
        ingredient: Ingredient,
        imageData: ByteArray?
    ): RequestState<Boolean> {
        var newImagePath: String? = null
        var oldImagePath: String? = null

        // 1. Manejo de la imagen: si se proporciona, guardar la nueva.
        if (imageData != null) {
            newImagePath = saveImageUseCase(imageData = imageData)

            if (newImagePath == null) {
                return RequestState.Error(
                    ResponseMessage(message = Res.string.unsupported_image_format)
                )
            }
        }

        return try {
            val newIngredient = ingredient.copy(imagePath = newImagePath ?: ingredient.imagePath)

            if (newIngredient.id == 0) {
                // Caso de CREACIÓN:
                // Solo crear el ingrediente en la base de datos.
                ingredientRepository.createIngredient(ingredient = newIngredient)
            } else {
                // Caso de EDICIÓN:
                // 1. Obtener la ruta de la imagen anterior antes de actualizar.
                val originalIngredientResult = ingredientRepository.getIngredientById(newIngredient.id)
                if (originalIngredientResult is RequestState.Success) {
                    oldImagePath = originalIngredientResult.data.imagePath
                } else {
                    // Si no se puede obtener el ingrediente original, la edición no puede continuar.
                    return originalIngredientResult as RequestState.Error
                }

                // 2. Actualizar el ingrediente en la base de datos.
                val updateResult = ingredientRepository.updateIngredient(ingredient = newIngredient)

                // 3. Si la actualización es exitosa, se elimina la imagen antigua.
                if (updateResult is RequestState.Success && oldImagePath != null && oldImagePath != newImagePath) {
                    imageSaver.deleteImage(oldImagePath)
                }

                // Si la actualización falla, el `catch` se encargará de la reversión.
                if (updateResult is RequestState.Error) {
                    return updateResult
                }
            }
            RequestState.Success(data = true)
        } catch (e: Exception) {
            Logger.e(e) { "Error inesperado al crear o actualizar ingrediente." }
            // Si algo falla, se borra la nueva imagen para evitar archivos huérfanos.
            if (newImagePath != null) {
                imageSaver.deleteImage(newImagePath)
            }
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error_create_ingredient))
        }
    }
}
