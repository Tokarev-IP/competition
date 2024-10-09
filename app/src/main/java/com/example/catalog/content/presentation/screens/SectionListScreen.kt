package com.example.catalog.content.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.MoreVert
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
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiIntents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.common.ErrorStateView
import com.example.catalog.content.presentation.common.LoadingStateView
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.presentation.views.SectionListView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SectionListScreen(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)
    val sectionList by contentViewModel.getSectionListFlow().collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarMsg by remember { mutableStateOf<String?>(null) }
    var isMenuExpanded by remember { mutableStateOf(false) }

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
        snackbarHost = {
            SnackbarHost(snackBarHostState) { snackbarData: SnackbarData ->
                Snackbar(modifier = modifier) {
                    Text(text = snackbarData.visuals.message)
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Sections") },
                actions = {
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
                            text = { Text("View menu") },
                            onClick = {
                                contentViewModel.setUiEvent(ContentUiEvents.ShowMenu)
                            },
                            enabled = (uiState is ContentUiStates.Show),
                        )
                        DropdownMenuItem(
                            text = { Text("Edit info images") },
                            onClick = {
                                contentViewModel.setUiEvent(ContentUiEvents.EditInfoImageList)
                            },
                            enabled = (uiState is ContentUiStates.Show),
                        )
                        DropdownMenuItem(
                            text = { Text("Edit menu info") },
                            onClick = {
                                contentViewModel.setUiEvent(ContentUiEvents.EditMenuInfo)
                            },
                            enabled = (uiState is ContentUiStates.Show),
                        )
                    }
                },
            )
        }
    ) { innerPadding ->

        when (uiState) {
            is ContentUiStates.Error -> {
                ErrorStateView(innerPadding = innerPadding) {
                    contentViewModel.setUiEvent(ContentUiEvents.DownloadDishAndSectionLists)
                }
            }

            is ContentUiStates.Show -> {
                SectionListView(
                    sectionList = sectionList,
                    innerPadding = innerPadding,
                    onEventHandler = { event: ContentUiEvents ->
                        contentViewModel.setUiEvent(event)
                    }
                )
            }

            is ContentUiStates.Loading -> {
                LoadingStateView(innerPadding = innerPadding)
            }
        }
    }
}