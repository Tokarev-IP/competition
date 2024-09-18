package com.example.catalog.content.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.catalog.content.presentation.views.EditInfoImageListView
import com.example.catalog.content.presentation.views.dialogs.ImageDialogView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditInfoImageListScreen(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
) {
    val infoImageList by contentViewModel.getInfoImageDataFlow().collectAsState()
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()

    var snackBarMsg by remember { mutableStateOf<String?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }
    var isOpenedImageDialog by rememberSaveable { mutableStateOf(false) }
    var imageUriDialog: Uri by rememberSaveable { mutableStateOf(Uri.EMPTY) }

    val pickImageLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            uri?.let { notNullUri ->
                contentViewModel.setUiEvent(ContentUiEvents.SaveInfoImage(imageUri = notNullUri))
            } ?: run { snackBarMsg = "No image was selected" }
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
                title = { Text(text = "Images") },
                navigationIcon = {
                    GoBackNavigationButton {
                        contentViewModel.setUiEvent(ContentUiEvents.GoBack)
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
                EditInfoImageListView(
                    innerPadding = innerPadding,
                    infoImageList = infoImageList,
                    onAddNewImage = {
                        pickImageLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onDeleteImage = { imageId ->
                        contentViewModel.setUiEvent(ContentUiEvents.DeleteInfoImage(imageId))
                    },
                    onClickImage = { imageUri ->
                        imageUriDialog = imageUri
                        isOpenedImageDialog = true
                    },
                )
            }

            is ContentUiStates.Error -> {
                ErrorStateView(innerPadding = innerPadding) {
                    contentViewModel.setUiEvent(ContentUiEvents.DownloadInfoImageList)
                }
            }
        }
    }

    if (isOpenedImageDialog)
        BasicAlertDialog(onDismissRequest = { isOpenedImageDialog = false }) {
            Surface(
                modifier = modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                ImageDialogView(
                    imageUri = imageUriDialog,
                    onConfirm = { isOpenedImageDialog = false })
            }
        }

}