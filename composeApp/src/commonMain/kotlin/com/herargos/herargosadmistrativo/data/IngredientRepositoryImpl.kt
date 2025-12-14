package com.herargos.herargosadmistrativo.data

import co.touchlab.kermit.Logger
import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.core.ResponseMessage
import com.herargos.herargosadmistrativo.data.database.AppDatabase
import com.herargos.herargosadmistrativo.domain.model.Ingredient
import com.herargos.herargosadmistrativo.domain.model.Recipe
import com.herargos.herargosadmistrativo.domain.repository.IngredientRepository
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.error
import herargosadministrativo.composeapp.generated.resources.unknown_error
import herargosadministrativo.composeapp.generated.resources.unknown_error_create_ingredient
import herargosadministrativo.composeapp.generated.resources.unknown_error_delete_ingredient
import herargosadministrativo.composeapp.generated.resources.unknown_error_reverting_ingredients
import herargosadministrativo.composeapp.generated.resources.unknown_error_update_ingredient
import herargosadministrativo.composeapp.generated.resources.unknown_error_updating_ingredients
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IngredientRepositoryImpl(private val dbHelper: AppDatabase) : IngredientRepository {
    override suspend fun createIngredient(ingredient: Ingredient): RequestState<Boolean> {
        return try {
            dbHelper.ingredientDao().createIngredient(ingredient.toEntity())
            RequestState.Success(data = true)
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error_create_ingredient))
        }
    }

    override suspend fun getIngredients(): Flow<RequestState<List<Ingredient>>> = flow {
        try {
            emit(RequestState.Loading)
            dbHelper.ingredientDao().getIngredients().collect { result ->
                emit(RequestState.Success(data = result.map { it.toDomain() }))
            }
        } catch (e: Exception) {
            emit(RequestState.Error(ResponseMessage(Res.string.error)))
        }

    }

    override suspend fun getIngredientById(id: Int): RequestState<Ingredient> {
        return try {
            val ingredient = dbHelper.ingredientDao().getIngredientById(id)
            RequestState.Success(data = ingredient.toDomain())
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(Res.string.error))
        }
    }

    override suspend fun deleteIngredient(ingredient: Ingredient): RequestState<Boolean> {
        try {
            dbHelper.ingredientDao().deleteIngredient(ingredient.toEntity().id)
            return RequestState.Success(data = true)
        } catch (e: Exception) {
            Logger.e(e) { "Error al eliminar ingrediente: ${ingredient.name}" }
            return RequestState.Error(ResponseMessage(message = Res.string.unknown_error_delete_ingredient))
        }
    }

    override suspend fun updateIngredient(ingredient: Ingredient): RequestState<Boolean> {
        try {
            dbHelper.ingredientDao().updateIngredient(ingredient.toEntity())
            return RequestState.Success(data = true)
        } catch (e: Exception) {
            Logger.e(e) { "Error al actualizar ingrediente: ${ingredient.name}" }
            return RequestState.Error(ResponseMessage(message = Res.string.unknown_error_update_ingredient))
        }
    }

    override suspend fun updateIngredientsStock(recipes: List<Recipe>): RequestState<Boolean> {
        return try {
            recipes.forEach { recipe ->
                dbHelper.ingredientDao().updateIngredientStock(recipe.idIngredient, recipe.quantity)
            }
            RequestState.Success(true)
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error_updating_ingredients))
        }
    }

    override suspend fun revertIngredientsStock(recipes: List<Recipe>): RequestState<Boolean> {
        return try {
            recipes.forEach { recipe ->
                dbHelper.ingredientDao().revertIngredientStock(recipe.idIngredient, recipe.quantity)
            }
            RequestState.Success(true)
        } catch (e: Exception) {
            RequestState.Error(ResponseMessage(message = Res.string.unknown_error_reverting_ingredients))
        }
    }
}