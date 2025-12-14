package com.herargos.herargosadmistrativo.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.herargos.herargosadmistrativo.ui.home.HomeScreen
import com.herargos.herargosadmistrativo.ui.ingredient.main.IngredientScreen
import com.herargos.herargosadmistrativo.ui.product.ProductScreen
import com.herargos.herargosadmistrativo.ui.sales.SaleScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Home) {
        composable<Routes.Home> {
            HomeScreen(
                toIngredient = { navController.navigate(Routes.Ingredient) },
                toProduct = { navController.navigate(Routes.Product) },
                toSale = { navController.navigate(Routes.Sale) }
            )
        }

        composable<Routes.Ingredient> {
            IngredientScreen(onBack = { navController.popBackStack() })
        }

        composable<Routes.Product> {
            ProductScreen(onBack = { navController.popBackStack() })
        }

        composable<Routes.Sale> {
            SaleScreen(onBack = { navController.popBackStack() })
        }
    }
}