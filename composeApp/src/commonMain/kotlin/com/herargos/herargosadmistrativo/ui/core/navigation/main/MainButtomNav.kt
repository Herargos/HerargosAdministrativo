package com.herargos.herargosadmistrativo.ui.core.navigation.main

import com.herargos.herargosadmistrativo.ui.core.navigation.Routes
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.ingredient
import herargosadministrativo.composeapp.generated.resources.product
import herargosadministrativo.composeapp.generated.resources.sales
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed class MainButtomNav {
    abstract val route: Any
    abstract val title: StringResource
    abstract val icon: DrawableResource

    data class Ingredient(
        override val route: Any = Routes.Ingredient,
        override val title: StringResource = Res.string.ingredient,
        override val icon: DrawableResource = Res.drawable.ingredient
    ) : MainButtomNav()

    data class Product(
        override val route: Any = Routes.Product,
        override val title: StringResource = Res.string.product,
        override val icon: DrawableResource = Res.drawable.product
    ):MainButtomNav()

    data class Sales(
        override val route: Any = Routes.Sale,
        override val title: StringResource = Res.string.sales,
        override val icon: DrawableResource = Res.drawable.sales
    ):MainButtomNav()
}