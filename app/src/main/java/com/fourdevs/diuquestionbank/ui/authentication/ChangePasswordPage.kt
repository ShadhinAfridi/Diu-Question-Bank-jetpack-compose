package com.fourdevs.diuquestionbank.ui.authentication

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.ui.navigation.ChangePassword
import com.fourdevs.diuquestionbank.ui.navigation.LogIn
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel

@Composable
fun ChangePasswordPage(navController: NavHostController, authViewModel: AuthViewModel) {
    ChangePassword(navController, authViewModel)
}

@Composable
private fun ChangePassword(navController: NavHostController, authViewModel: AuthViewModel) {

    val passwordChangeFlow = authViewModel.passwordChangeFlow.collectAsState()
    var loading by remember { mutableStateOf(false) }
    var buttonClicked by remember { mutableStateOf(false) }

    AuthBackground(stringResource(id = R.string.reset_npassword)) {
        var password by remember {
            mutableStateOf("")
        }
        var confirmPassword by remember {
            mutableStateOf("")
        }
        val localFocusManager = LocalFocusManager.current
        val context = LocalContext.current

        AuthTextField(
            label = Constants.DATA_PASSWORD,
            keyboardActions = KeyboardActions(onNext = {
                localFocusManager.moveFocus(FocusDirection.Down)
            })
        ){
            password = it
        }

        Spacer(modifier = Modifier.height(20.dp))

        AuthTextField(
            label = Constants.DATA_CONFIRM_PASSWORD,
            keyboardActions = KeyboardActions(onNext = {
                localFocusManager.clearFocus(true)
            }),
            imeAction = ImeAction.Done
        ){
            confirmPassword = it
        }

        Spacer(modifier = Modifier.height(30.dp))

        PrimaryColorButton(label = "Change") {
            if(authViewModel.checkInternetConnection()) {
                if(password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                    if(password.length>=6 && confirmPassword.length>=6 && password==confirmPassword) {
                        authViewModel.updateUserPassword(authViewModel.getString(Constants.KEY_EMAIL)!!, password)
                        buttonClicked = true
                    } else {
                        showToast(context,"Password don't match!")
                    }
                }
            } else {
                showNoInternet(context)
            }
        }

        if(buttonClicked) {
            passwordChangeFlow.value?.let {
                when (it) {
                    is Resource.Failure -> {
                        ShowToast(message = it.exception.message.toString(), LocalContext.current)
                        loading = !loading
                        buttonClicked = !buttonClicked
                    }
                    is Resource.Loading -> {
                        loading = true
                    }
                    is Resource.Success -> {
                        buttonClicked = !buttonClicked
                        ShowToast(message = "Successfully password changed!", LocalContext.current)
                        LaunchedEffect(Unit) {
                            navController.navigate(LogIn.route) {
                                popUpTo(ChangePassword.route) { inclusive = true }
                            }
                        }
                    }
                }
            }
        }


    }

    if(loading) {
        ProgressBar()
    }
}