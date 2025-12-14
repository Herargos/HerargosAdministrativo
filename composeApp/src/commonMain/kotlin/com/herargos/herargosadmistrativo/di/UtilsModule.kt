package com.herargos.herargosadmistrativo.di

import com.herargos.herargosadmistrativo.core.utils.DefaultMessages
import com.herargos.herargosadmistrativo.core.utils.Messages
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val utilsModule: Module = module {
    singleOf(::DefaultMessages) bind Messages::class
}