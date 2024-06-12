package com.example.catalog.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.catalog.content.ContentActivity
import com.example.catalog.login.data.PhoneAuthProviderCallback
import com.example.catalog.login.presentation.LoginActivityCompose
import com.example.catalog.login.presentation.LoginUiEvents
import com.example.catalog.login.presentation.LoginUiIntents
import com.example.catalog.login.presentation.LoginViewModel
import com.example.catalog.login.ui.theme.CatalogTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        setContent {
            CatalogTheme {
                    LoginActivityCompose(
                        onPhoneLogin = { phoneNumber: String ->
                            val phoneAuthOptions = getPhoneAuthOptions(
                                phoneNumber = phoneNumber,
                                callback = loginViewModel.getPhoneAuthProviderCallback(),
                            )
                            loginViewModel.setUiEvent(
                                LoginUiEvents.SendLoginSmsCode(
                                    phoneAuthOptions
                                )
                            )
                        },
                        loginViewModel = loginViewModel,
                    )

            }
        }

        lifecycleScope.launch {
            loginViewModel.getUiIntentsFlow().collect { intent ->
                when (intent) {
                    is LoginUiIntents.GoToContentActivity -> {
                        loginViewModel.clearUiIntents()
                        goToContentActivity()
                    }
                }
            }
        }
    }

    private fun goToContentActivity() {
        val intent = Intent(this, ContentActivity::class.java)
        startActivity(intent)
    }

    private fun getPhoneAuthOptions(
        phoneNumber: String,
        callback: PhoneAuthProviderCallback
    ): PhoneAuthOptions {
        val auth = FirebaseAuth.getInstance()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callback) // OnVerificationStateChangedCallbacks
            .build()

        return options
    }
}
