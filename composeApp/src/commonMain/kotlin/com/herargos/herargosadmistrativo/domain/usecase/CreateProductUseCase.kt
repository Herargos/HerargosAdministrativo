package com.herargos.herargosadmistrativo.domain.usecase

import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.core.ResponseMessage
import com.herargos.herargosadmistrativo.data.filesaver.ImageSaver
import com.herargos.herargosadmistrativo.domain.model.Product
import com.herargos.herargosadmistrativo.domain.repository.IngredientRepository
import com.herargos.herargosadmistrativo.domain.repository.ProductRepository
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.unknown_error_create_ingredient
import herargosadministrativo.composeapp.generated.resources.unsupported_image_format

class CreateProductUseCase(
    private val productRepository: ProductRepository,
    private val ingredientRepository: IngredientRepository,
    private val saveImageUseCase: SaveImageUseCase,
    private val imageSaver: ImageSaver
) {
    suspend operator fun invoke(
        product: Product,
        imageData: ByteArray?
    ): RequestState<Boolean> {
        var imagePath: String? = null

        if (imageData != null) {
            // Llama al UseCase para guardar la imagen.
            imagePath = saveImageUseCase(imageData = imageData)

            // Si la imagen no se pudo guardar (retornó null), detén el proceso y lanza un error.
            if (imagePath == null) {
                return RequestState.Error(
                    ResponseMessage(message = Res.string.unsupported_image_format)
                )
            }
        }

        // Se usa `try-catch` para asegurar que el stock se revierta
        // y la imagen se elimine si algo falla.
        return try {
            val newProduct = product.copy(imagePath = imagePath ?: "")

            if (newProduct.id != 0) {
                // Caso de EDICIÓN:
                // 1. Obtener el producto original para conocer las recetas previas.
                val originalProductResult = productRepository.getProductById(newProduct.id)
                if (originalProductResult is RequestState.Error) {
                    return originalProductResult
                }
                val originalProduct = (originalProductResult as RequestState.Success).data

                // 2. Revertir el stock de los ingredientes de la receta original.
                val revertResult =
                    ingredientRepository.revertIngredientsStock(originalProduct.recipes)
                if (revertResult is RequestState.Error) {
                    return revertResult
                }
            }

            // 3. Restar el stock de los ingredientes de la NUEVA receta.
            if (newProduct.recipes.isNotEmpty()) {
                val updateStockResult =
                    ingredientRepository.updateIngredientsStock(newProduct.recipes)
                if (updateStockResult is RequestState.Error) {
                    return updateStockResult
                }
            }

            // 4. Crea o actualiza el producto en la base de datos.
            val productResult = if (newProduct.id == 0) {
                productRepository.createProduct(product = newProduct)
            } else {
                productRepository.updateProduct(product = newProduct)
            }

            // 5. Si la creación/actualización falla, el `catch` se encargará de la reversión.
            if (productResult is RequestState.Error) {
                return productResult
            }

            // 6. Si todo es exitoso, se devuelve true.
            RequestState.Success(data = true)
        } catch (e: Exception) {
            // Si cualquier paso falla, se revierte el stock de la NUEVA receta
            // y se elimina la imagen para mantener la consistencia.
            if (product.recipes.isNotEmpty()) {
                ingredientRepository.revertIngredientsStock(product.recipes)
            }
            if (imagePath != null) {
                imageSaver.deleteImage(imagePath)
            }
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error_create_ingredient))
        }
    }
}
