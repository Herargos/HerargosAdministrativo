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

@Composable
fun App(messages: Messages = koinInject()) {
    AppTheme {
        val scope = rememberCoroutineScope()
        val snackBarHostState = remember { SnackbarHostState() }
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
                NavigationWrapper()
            }
        }
    }
}