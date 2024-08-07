package com.example.catalog.content.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentViewModel
import com.example.catalog.content.presentation.views.ChooseLanguageDialogView
import com.example.catalog.content.presentation.views.MenuListView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MenuListScreen(
    contentViewModel: ContentViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by contentViewModel.getUiStatesFlow().collectAsState()
    val dishList by contentViewModel.getDishListFlow().collectAsState()

    var isOpenedLanguageDialog by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf<String?>(null) }

    val chooseFolderLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
            if (uri != null) {
                isOpenedLanguageDialog = false

                contentViewModel.setUiEvent(
                    ContentUiEvents.SaveMenuAsDocFile(
                        uri,
                        selectedLanguage
                    )
                )
            }
        }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Menu")},
                actions = {
                    IconButton(
                        onClick = {
                            isExpanded = !isExpanded
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "Go back"
                        )
                    }

                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = { /* Handle edit! */ },
                            leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = { /* Handle settings! */ },
                            leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Send Feedback") },
                            onClick = { /* Handle send feedback! */ },
                            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                            trailingIcon = { Text("F11", textAlign = TextAlign.Center) }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        MenuListView(
            uiState = uiState,
            dishList = dishList,
            eventHandler = { contentUiEvents: ContentUiEvents ->
                contentViewModel.setUiEvent(contentUiEvents)
            },
            onCreateMenuDoc = {
                isOpenedLanguageDialog = true
            },
            innerPadding = innerPadding
        )

        if (isOpenedLanguageDialog)
            BasicAlertDialog(onDismissRequest = { isOpenedLanguageDialog = false }) {
                Surface(
                    modifier = modifier.clip(RoundedCornerShape(16.dp))
                ) {
                    ChooseLanguageDialogView(
                        onCancel = { isOpenedLanguageDialog = false },
                        onAccept = { language: String? ->
                            if (language != null) {
                                selectedLanguage = language
                            }
                            chooseFolderLauncher.launch(null)
                        }
                    )
                }
            }

    }

    LaunchedEffect(key1 = Unit) {
        contentViewModel.setUiEvent(ContentUiEvents.DownloadMenuList)
    }
}