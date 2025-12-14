package com.herargos.herargosadmistrativo.domain.usecase

import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.core.ResponseMessage
import com.herargos.herargosadmistrativo.core.utils.dateTimeNow
import com.herargos.herargosadmistrativo.domain.model.Sale
import com.herargos.herargosadmistrativo.domain.repository.ProductRepository
import com.herargos.herargosadmistrativo.domain.repository.SaleRepository
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.unknown_error_create_sale
import herargosadministrativo.composeapp.generated.resources.unknown_error_update_sale
import herargosadministrativo.composeapp.generated.resources.error_updating_product_stock
import herargosadministrativo.composeapp.generated.resources.unknown_error

class CreateSaleUseCase(
    private val saleRepository: SaleRepository,
    private val productRepository: ProductRepository
) {
    /**
     * Disminuye el stock de los productos vendidos.
     */
    private suspend fun decreaseProductStockForSale(sale: Sale): RequestState<Boolean> {
        for (line in sale.lines) {
            val result = productRepository.decreaseProductStock(line.idProduct, line.productStock)
            if (result is RequestState.Error) {
                // Si falla, intenta revertir los que ya se restaron en este bucle
                val alreadyDecreasedLines = sale.lines.takeWhile { it.id != line.id }
                for (revertLine in alreadyDecreasedLines) {
                    productRepository.increaseProductStock(revertLine.idProduct, revertLine.productStock)
                }
                return RequestState.Error(
                    ResponseMessage(
                        message = Res.string.error_updating_product_stock,
                        description = "Fallo al restar stock del producto ID: ${line.idProduct}. No hay stock suficiente."
                    )
                )
            }
        }
        return RequestState.Success(data = true)
    }

    /**
     * Aumenta el stock de los productos (para revertir una venta).
     */
    private suspend fun increaseProductStockForSale(sale: Sale): RequestState<Boolean> {
        for (line in sale.lines) {
            val result = productRepository.increaseProductStock(line.idProduct, line.productStock)
            if (result is RequestState.Error) {
                // Este error es menos probable pero debe ser manejado.
                return RequestState.Error(
                    ResponseMessage(
                        message = Res.string.error_updating_product_stock,
                        description = "Fallo al revertir stock del producto ID: ${line.idProduct}"
                    )
                )
            }
        }
        return RequestState.Success(data = true)
    }

    /**
     * Crea o actualiza una venta, gestionando de forma transaccional la actualización del stock de productos.
     */
    suspend operator fun invoke(sale: Sale): RequestState<Boolean> {
        val isUpdate = sale.id != 0
        val newSale = if (isUpdate) sale else sale.copy(createDate = dateTimeNow())

        // 1. Si es una actualización, obtener la venta original.
        val originalSale: Sale? = if (isUpdate) {
            when (val result = saleRepository.getSaleByIdSingle(newSale.id)) {
                is RequestState.Success -> result.data
                is RequestState.Error -> return result // No se puede continuar si no se encuentra la venta original
                else -> return RequestState.Error(ResponseMessage(message = Res.string.unknown_error))
            }
        } else {
            null
        }

        // --- INICIO DE LA TRANSACCIÓN ---

        // 2. Si es una actualización, revertir (sumar) el stock de la venta original.
        if (originalSale != null) {
            val revertResult = increaseProductStockForSale(originalSale)
            if (revertResult is RequestState.Error) {
                return revertResult // Error crítico: no se pudo revertir el stock antiguo
            }
        }

        // 3. Restar el stock para la nueva venta (sea nueva o actualizada).
        val decreaseResult = decreaseProductStockForSale(newSale)
        if (decreaseResult is RequestState.Error) {
            // Si falla, y era una actualización, tenemos que re-aplicar el stock original para deshacer la reversión.
            if (originalSale != null) {
                decreaseProductStockForSale(originalSale)
            }
            return decreaseResult
        }

        // 4. Crear o actualizar la venta en la base de datos.
        val saleResult = if (isUpdate) {
            saleRepository.updateSale(sale = newSale)
        } else {
            saleRepository.createSale(sale = newSale)
        }

        // 5. Si la persistencia de la venta falla, revertir TODOS los cambios de stock.
        if (saleResult is RequestState.Error) {
            // Revertir la resta de stock de la nueva venta.
            increaseProductStockForSale(newSale)
            // Si era una actualización, también revertir la suma de stock que hicimos al principio.
            if (originalSale != null) {
                decreaseProductStockForSale(originalSale)
            }
            return saleResult // Devolver el error original de la base de datos
        }

        // --- FIN DE LA TRANSACCIÓN (ÉXITO) ---
        return RequestState.Success(data = true)
    }
}
