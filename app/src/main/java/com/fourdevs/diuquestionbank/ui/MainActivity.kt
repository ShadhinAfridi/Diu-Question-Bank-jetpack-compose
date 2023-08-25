package com.fourdevs.diuquestionbank.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.fourdevs.diuquestionbank.ui.components.App
import com.fourdevs.diuquestionbank.ui.theme.DIUQuestionBankTheme
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel : AuthViewModel by viewModels()
    private val questionViewModel : QuestionViewModel by viewModels()
    private val userViewModel : UserViewModel by viewModels()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent() {
            if(authViewModel.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                authViewModel.currentUser?.let {
                    authViewModel.getIdToken(it)
                }
            }
            DIUQuestionBankTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(
                        navController = rememberNavController(),
                        authViewModel = authViewModel,
                        questionViewModel = questionViewModel,
                        userViewModel = userViewModel
                    )
                }
            }
        }
    }
}
