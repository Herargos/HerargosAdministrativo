package com.herargos.herargosadmistrativo.di

import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientViewModel
import com.herargos.herargosadmistrativo.ui.product.ProductViewModel
import com.herargos.herargosadmistrativo.ui.sales.SaleViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::IngredientViewModel)
    viewModelOf(::ProductViewModel)
    viewModelOf(::SaleViewModel)
}