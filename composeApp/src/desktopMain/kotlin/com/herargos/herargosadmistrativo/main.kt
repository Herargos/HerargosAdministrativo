package com.herargos.herargosadmistrativo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.herargos.herargosadmistrativo.di.initKoin
import herargosadministrativo.composeapp.generated.resources.Res
import herargosadministrativo.composeapp.generated.resources.app_icon
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.window.WindowPlacement



fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PanOregano",
        icon = painterResource(Res.drawable.app_icon),
        state = rememberWindowState(placement = WindowPlacement.Maximized)
    ) {
        initKoin()
        App()
    }
}