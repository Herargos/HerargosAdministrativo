package com.herargos.herargosadmistrativo.domain.repository

import com.herargos.herargosadmistrativo.core.RequestState
import com.herargos.herargosadmistrativo.domain.model.Ingredient
import com.herargos.herargosadmistrativo.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface IngredientRepository {
    suspend fun createIngredient(ingredient: Ingredient): RequestState<Boolean>
    suspend fun getIngredients(): Flow<RequestState<List<Ingredient>>>
    suspend fun getIngredientById(id: Int): RequestState<Ingredient>
    suspend fun deleteIngredient(ingredient: Ingredient): RequestState<Boolean>
    suspend fun updateIngredient(ingredient: Ingredient): RequestState<Boolean>
    suspend fun updateIngredientsStock(recipes: List<Recipe>): RequestState<Boolean>
    suspend fun revertIngredientsStock(recipes: List<Recipe>): RequestState<Boolean>
}