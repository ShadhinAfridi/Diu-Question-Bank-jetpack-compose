package com.fourdevs.diuquestionbank.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.fourdevs.diuquestionbank.ui.components.App
import com.fourdevs.diuquestionbank.ui.theme.DIUQuestionBankTheme
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel
import com.fourdevs.diuquestionbank.viewmodel.NotificationViewModel
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val questionViewModel: QuestionViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (authViewModel.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                // Perform background tasks using a coroutine
                lifecycleScope.launch {
                    authViewModel.currentUser?.let {
                        authViewModel.getIdToken(it)
                    }
                }
            }
            val systemTheme = userViewModel.systemThemeFlow.collectAsState()
            DIUQuestionBankTheme(systemTheme.value) {
                // Optimize the composition by separating it into functions
                MainScreen(
                    authViewModel = authViewModel,
                    questionViewModel = questionViewModel,
                    userViewModel = userViewModel,
                    notificationViewModel = notificationViewModel
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    questionViewModel: QuestionViewModel,
    userViewModel: UserViewModel,
    notificationViewModel: NotificationViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Optimize the content of your composition
        App(
            navController = rememberNavController(),
            authViewModel = authViewModel,
            questionViewModel = questionViewModel,
            userViewModel = userViewModel,
            notificationViewModel = notificationViewModel
        )
    }
}