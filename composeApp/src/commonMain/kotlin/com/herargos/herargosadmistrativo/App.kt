package com.herargos.herargosadmistrativo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.herargos.herargosadmistrativo.core.utils.Messages
import com.herargos.herargosadmistrativo.core.utils.ObserveAsEvents
import com.herargos.herargosadmistrativo.ui.core.navigation.NavigationWrapper
import com.herargos.herargosadmistrativo.ui.core.theme.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.koinInject

/**
 * Main composable representing the root of the application.
 *
 * It configures the application's theme, a base Scaffold with a Snackbar to display global
 * messages, and the container for the application's navigation.
 *
 * @param messages Instance of [Messages] injected by Koin, used to listen for and display
 * messages (Snackbars) throughout the application.
 */
@Composable
fun App(messages: Messages = koinInject()) {
    AppTheme {
        val scope = rememberCoroutineScope()
        val snackBarHostState = remember { SnackbarHostState() }

        // Observes the message flow and displays them in a Snackbar.
        ObserveAsEvents(
            flow = messages.messages,
        ) { msg ->
            scope.launch {
                snackBarHostState.showSnackbar(
                    "${msg.message?.let { getString(it) }} ${msg.description ?: ""}"
                )
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                // The NavigationWrapper component manages the different screens of the app.
                NavigationWrapper()
            }
        }
    }
}
