package com.example.catalog.content

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.example.catalog.content.presentation.ContentActivityCompose
import com.example.catalog.content.presentation.ContentViewModel
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
//                ContentActivityCompose(
//                    contentViewModel = contentViewModel,
//                )

                test(
                    test1 = {
                        Log.d("TESTLOG", "test 1")
                        Text(text = "test 1")
                    },
                    test2 = {
                        Log.d("TESTLOG", "test 2 string")
                        Text(text = "test 2")
                        "test 2 string"
                    },
                    test3 = {
                        Log.d("TESTLOG", it)
                    },
                    test4 = {
                        Log.d("TESTLOG", "4")
                    },
                    test6 = {
                        Log.d("TESTLOG", "test 6 string")
                        "test 6 string"
                    },
                ){
                    Log.d("TESTLOG", it)
                }
            }
        }

    }

    @Composable
    fun test(
        test1: @Composable () -> Unit,
        test2: @Composable () -> String,
        test3: (String) -> Unit,
        test4: (Unit) -> Unit = {
            Log.d("TESTLOG", "test 444")
        },
        test5: () -> Unit = {
            Log.d("TESTLOG", "test 555")
        },
        test6: () -> String = { "test6" },
        test7: (a: String) -> Unit,
    ) {
        test1()
        test2()
        test3("test3")
        test4(Unit)
        test5()
        test6()
        test7("test7")
    }
}