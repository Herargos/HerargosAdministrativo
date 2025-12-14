package com.herargos.herargosadmistrativo.di

import com.herargos.herargosadmistrativo.data.database.AppDatabase
import com.herargos.herargosadmistrativo.data.database.getDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platfromModule(): Module {
    return module {
        single<AppDatabase> {
            getDatabase(get())
        }
    }
}