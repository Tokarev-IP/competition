package com.example.catalog.content.presentation.base

import androidx.lifecycle.ViewModel
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.SectionDataFirebase
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiIntents
import com.example.catalog.content.presentation.ContentUiStates
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class ContentBaseViewModel<uiState : ContentUiStates, uiIntents : ContentUiIntents, uiEvent : ContentUiEvents>(
    initialState: uiState
) : ViewModel() {

    private val uiStates = MutableStateFlow(initialState)
    private val uiStatesFlow = uiStates.asStateFlow()

    private val uiIntents = MutableSharedFlow<uiIntents?>(
        replay = 1,
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
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

    private val dishItemData = MutableStateFlow<DishData>(DishData())
    private val dishItemDataFlow = dishItemData.asStateFlow()

    private val dishList = MutableStateFlow<List<DishData>>(emptyList())
    private val dishListFlow = dishList.asStateFlow()

    private val sectionList = MutableStateFlow<List<SectionDataFirebase>>(emptyList())
    private val sectionListFlow = sectionList.asStateFlow()

    protected fun setDishItemData(data: DishData) {
        dishItemData.value = data
    }

    protected fun setDishList(list: List<DishData>) {
        dishList.value = list
    }

    protected fun setSectionList(list: List<SectionDataFirebase>) {
        sectionList.value = list
    }

    protected fun getDishItemData() = dishItemData.value
    protected fun getDishList() = dishList.value
    protected fun getSectionList() = sectionList.value

    fun getDishItemDataFlow() = dishItemDataFlow
    fun getDishListFlow() = dishListFlow
    fun getSectionListFlow() = sectionListFlow
}