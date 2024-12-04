package com.example.catalog.content.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiIntents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.common.ErrorStateView
import com.example.catalog.content.presentation.common.GoBackNavigationButton
import com.example.catalog.content.presentation.common.LoadingStateView
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.presentation.views.MenuView
import com.example.catalog.content.presentation.views.dialogs.ChooseLanguageDialogView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MenuScreen(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val menuDishData by contentViewModel.getMenuViewDataFlow().collectAsState()
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)

    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarMsg by remember { mutableStateOf<String?>(null) }

    var isOpenedLanguageDialog by remember { mutableStateOf(false) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf<String?>(null) }

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
                title = { Text(text = "Menu") },
                navigationIcon = {
                    GoBackNavigationButton {
                        contentViewModel.setUiEvent(ContentUiEvents.GoBack)
                    }
                },
                actions = {
                    if (menuDishData.dishListAndSectionViewDataList.isNotEmpty()) {
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
        }
    ) { innerPadding ->
        when (uiState) {
            is ContentUiStates.Show -> {
                MenuView(
                    dishListAndSectionViewDataList = menuDishData.dishListAndSectionViewDataList,
                    menuInfoData = menuDishData.menuInfoData,
                    infoImageList = menuDishData.infoImageList,
                    innerPadding = innerPadding,
                )
            }
            is ContentUiStates.Loading -> {
                LoadingStateView(innerPadding = innerPadding)
            }
            is ContentUiStates.Error -> {
                ErrorStateView(innerPadding = innerPadding){
                    contentViewModel.setUiEvent(ContentUiEvents.DownloadMenu)
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
}