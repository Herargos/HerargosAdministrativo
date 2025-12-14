package com.herargos.herargosadmistrativo.di

import com.herargos.herargosadmistrativo.data.ClientRepositoryImpl
import com.herargos.herargosadmistrativo.data.IngredientRepositoryImpl
import com.herargos.herargosadmistrativo.data.ProductRepositoryImpl
import com.herargos.herargosadmistrativo.data.SaleRepositoryImpl
import com.herargos.herargosadmistrativo.data.filesaver.ImageSaver
import com.herargos.herargosadmistrativo.data.filesaver.ImageSaverImpl
import com.herargos.herargosadmistrativo.domain.repository.ClientRepository
import com.herargos.herargosadmistrativo.domain.repository.IngredientRepository
import com.herargos.herargosadmistrativo.domain.repository.ProductRepository
import com.herargos.herargosadmistrativo.domain.repository.SaleRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    factoryOf(::IngredientRepositoryImpl) bind IngredientRepository::class
    factoryOf(::ProductRepositoryImpl) bind ProductRepository::class
    factoryOf(::ClientRepositoryImpl) bind ClientRepository::class
    factoryOf(::SaleRepositoryImpl) bind SaleRepository::class
    factoryOf(::ImageSaverImpl) bind ImageSaver::class
}