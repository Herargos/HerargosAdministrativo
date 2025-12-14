package com.herargos.herargosadmistrativo.ui.core.navigation.main

import com.herargos.herargosadmistrativo.ui.core.navigation.main.MainButtomNav.*

fun providerMainNav(): List<MainButtomNav>{
    return listOf(Ingredient(), Product(), Sales())
}