package com.example.catalog.content.presentation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<uiState : Any, uiIntents : Any, uiEvent : Any>(
    initialState: uiState
) : ViewModel() {

    private val uiStates = MutableStateFlow<uiState>(initialState)
    private val uiStatesFlow = uiStates.asStateFlow()

    private val uiIntents = MutableSharedFlow<uiIntents?>(replay = 1, extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val uiIntentsFlow = uiIntents.asSharedFlow()

    protected fun setUiState(state: uiState) {
        uiStates.value = state
    }

    protected fun setUiIntent(intent: uiIntents) {
        uiIntents.tryEmit(intent)
    }

    fun getUiStatesFlow() = uiStatesFlow
    fun getUiIntentsFlow() = uiIntentsFlow

    fun clearUiIntents() {
        uiIntents.tryEmit(null)
    }

    abstract fun setUiEvent(uiEvent: uiEvent)
}