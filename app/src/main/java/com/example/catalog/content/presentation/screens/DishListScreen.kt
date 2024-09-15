package com.example.catalog.content.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Alignment
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
import com.example.catalog.content.presentation.views.dialogs.ChooseLanguageDialogView
import com.example.catalog.content.presentation.views.dialogs.DishReviewDialogView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DishListScreen(
    contentViewModel: ContentViewModel,
    modifier: Modifier = Modifier,
    sectionData: SectionData,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val dishList by contentViewModel.getDishListFlow().collectAsState()
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)

    var isOpenedLanguageDialog by remember { mutableStateOf(false) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isOpenedDishViewDialog by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf<String?>(null) }
    var dishData by remember { mutableStateOf<DishData?>(null) }
    var snackBarMsg by remember { mutableStateOf<String?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }

    val chooseFolderLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
            uri?.let { notNullUri ->
                contentViewModel.setUiEvent(
                    ContentUiEvents.SaveMenuAsPdfFile(
                        notNullUri,
                        selectedLanguage
                    )
                )
            } ?: run { snackBarMsg = "No folder was selected" }
        }

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
                actions = {
                    if (dishList.isNotEmpty()) {
                        IconButton(
                            onClick = { isMenuExpanded = !isMenuExpanded }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MoreVert,
                                contentDescription = "Open action menu"
                            )
                        }
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Save menu as PDF file") },
                                onClick = {
                                    isMenuExpanded = false
                                    selectedLanguage = null
                                    chooseFolderLauncher.launch(null)
                                },
                                enabled = (uiState is ContentUiStates.Show),
                            )
                            DropdownMenuItem(
                                text = { Text("Save translated menu as PDF file") },
                                onClick = {
                                    isMenuExpanded = false
                                    isOpenedLanguageDialog = true
                                },
                                enabled = (uiState is ContentUiStates.Show),
                            )
                        }
                    }
                }
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

    if (isOpenedLanguageDialog)
        BasicAlertDialog(onDismissRequest = { isOpenedLanguageDialog = false }) {
            Surface(
                modifier = modifier.clip(RoundedCornerShape(16.dp))
            ) {
                ChooseLanguageDialogView(
                    onCancel = { isOpenedLanguageDialog = false },
                    onAccept = { language: String? ->
                        isOpenedLanguageDialog = false
                        if (language != null) {
                            selectedLanguage = language
                        }
                        chooseFolderLauncher.launch(null)
                    }
                )
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