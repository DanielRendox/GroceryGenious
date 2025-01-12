package com.rendox.grocerygenius.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

interface UiEvent<T> {
    val data: T
    fun onConsumed()
}

@Composable
fun <T> ObserveUiEvent(
    event: UiEvent<T>?,
    onEvent: suspend (T) -> Unit
) {
    LaunchedEffect(event) {
        if (event != null) {
            onEvent(event.data)
            event.onConsumed()
        }
    }
}