package com.herargos.herargosadmistrativo.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herargos.herargosadmistrativo.core.exportDatabase
import com.herargos.herargosadmistrativo.ui.core.navigation.main.MainButtomNav
import com.herargos.herargosadmistrativo.ui.core.navigation.main.providerMainNav
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(toIngredient: () -> Unit, toProduct: () -> Unit, toSale: () -> Unit) {
    val items = providerMainNav()
    val recipe = MainButtomNav.Ingredient()
    val product = MainButtomNav.Product()
    val sales = MainButtomNav.Sales()

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.weight(1F)) {
                ElevatedCard(modifier = Modifier.weight(1F).padding(8.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize().clickable { toIngredient() }
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(75.dp),
                            contentDescription = stringResource(recipe.title),
                            painter = painterResource(recipe.icon)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(recipe.title),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
                ElevatedCard(modifier = Modifier.weight(1F).padding(8.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize().clickable { toProduct() }.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(75.dp),
                            contentDescription = stringResource(product.title),
                            painter = painterResource(product.icon)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(product.title),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
            }
            Row(modifier = Modifier.weight(1F)) {
                ElevatedCard(modifier = Modifier.weight(1F).padding(8.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize().clickable { toSale() }.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(75.dp),
                            contentDescription = stringResource(sales.title),
                            painter = painterResource(sales.icon)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(sales.title),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
                ElevatedCard(modifier = Modifier.weight(1F).padding(8.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize().clickable { exportDatabase() }
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(75.dp),
                            contentDescription = stringResource(sales.title),
                            painter = painterResource(sales.icon)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Respaldo",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
            }
        }
    }
}