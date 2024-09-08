package com.example.catalog.content

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import com.example.catalog.content.presentation.ContentActivityCompose
import com.example.catalog.content.presentation.viewmodel.ContentViewModel
import com.example.catalog.content.ui.theme.CatalogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentActivity : ComponentActivity() {

    private lateinit var contentViewModel: ContentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        contentViewModel = ViewModelProvider(this)[ContentViewModel::class.java]

        setContent {
            CatalogTheme {
                Surface {
                    ContentActivityCompose(
                        contentViewModel = contentViewModel,
                    )
                }
            }
        }
    }
}