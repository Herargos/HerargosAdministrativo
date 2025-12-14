package com.herargos.herargosadmistrativo.di

import com.herargos.herargosadmistrativo.domain.usecase.CreateIngredientUseCase
import com.herargos.herargosadmistrativo.domain.usecase.CreateProductUseCase
import com.herargos.herargosadmistrativo.domain.usecase.CreateSaleUseCase
import com.herargos.herargosadmistrativo.domain.usecase.SaveImageUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::SaveImageUseCase)
    factoryOf(::CreateIngredientUseCase)
    factoryOf(::CreateProductUseCase)
    factoryOf(::CreateSaleUseCase)
}