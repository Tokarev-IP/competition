package com.example.catalog.content.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiIntents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.common.ErrorStateView
import com.example.catalog.content.presentation.common.GoBackNavigationButton
import com.example.catalog.content.presentation.common.LoadingStateView
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.presentation.views.DishListView
import com.example.catalog.content.presentation.views.dialogs.DishReviewDialogView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DishListScreen(
    contentViewModel: ContentViewModel,
    modifier: Modifier = Modifier,
    sectionData: SectionData,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val dishList by contentViewModel.getDishListForSpecificSectionFlow().collectAsState()
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)

    var isOpenedDishViewDialog by remember { mutableStateOf(false) }
    var dishData by remember { mutableStateOf<DishData?>(null) }
    var snackBarMsg by remember { mutableStateOf<String?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }

    if (uiIntent is ContentUiIntents.ShowSnackBarMsg) {
        snackBarMsg = (uiIntent as ContentUiIntents.ShowSnackBarMsg).msg
        contentViewModel.clearUiIntents()
    }

    LaunchedEffect(key1 = snackBarMsg) {
        snackBarMsg?.let { msg ->
            snackBarHostState.showSnackbar(
                message = msg,
                actionLabel = "Result of action",
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = sectionData.name) },
                navigationIcon = {
                    GoBackNavigationButton {
                        contentViewModel.setUiEvent(ContentUiEvents.GoBack)
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState) { snackbarData: SnackbarData ->
                Snackbar(modifier = modifier) {
                    Text(text = snackbarData.visuals.message)
                }
            }
        },
    ) { innerPadding ->

        when (uiState) {
            is ContentUiStates.Loading -> {
                LoadingStateView(innerPadding = innerPadding)
            }

            is ContentUiStates.Show -> {
                DishListView(
                    dishList = dishList,
                    eventHandler = { contentUiEvents: ContentUiEvents ->
                        contentViewModel.setUiEvent(contentUiEvents)
                    },
                    innerPadding = innerPadding,
                    onCardClick = { data: DishData ->
                        dishData = data
                        isOpenedDishViewDialog = true
                    },
                    sectionId = sectionData.id,
                )
            }

            is ContentUiStates.Error -> {
                ErrorStateView(innerPadding = innerPadding) {
                    contentViewModel.setUiEvent(ContentUiEvents.DownloadMenuList)
                }
            }
        }
    }

    if (isOpenedDishViewDialog)
        dishData?.let { data ->
            BasicAlertDialog(onDismissRequest = { isOpenedDishViewDialog = false }) {
                Surface(
                    modifier = modifier.clip(RoundedCornerShape(16.dp))
                ) {
                    DishReviewDialogView(
                        dishData = data,
                        onAccepted = { isOpenedDishViewDialog = false }
                    )
                }
            }
        }
}