package com.fourdevs.diuquestionbank.ui.authentication

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.ui.navigation.ResetPassword
import com.fourdevs.diuquestionbank.ui.navigation.Verification
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel

@Composable
fun RecoverPassword(navController: NavHostController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    val localFocusManager = LocalFocusManager.current
    var buttonClicked by remember { mutableStateOf(false) }
    val networkResponse by authViewModel.networkResponseFlow.collectAsState()
    val randomNumber = authViewModel.generateRandomNumber()
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current


    AuthBackground(stringResource(id = R.string.reset_npassword)) {
        email = emailTextField(localFocusManager = localFocusManager)
        Spacer(modifier = Modifier.height(20.dp))

        PrimaryColorButton(label = stringResource(id = R.string.reset)) {

            if(authViewModel.checkInternetConnection()) {
                if(email.isNotEmpty() && isValidEmail(email)) {
                    authViewModel.getUserByEmail(email)
                    buttonClicked = true
                } else {
                    showToast(context,"Enter a valid email!")
                }
            } else {
                showNoInternet(context)
            }


        }

        if(buttonClicked) {

            networkResponse?.let {
                when (it) {
                    is Resource.Failure -> {
                        ShowToast(message = it.exception.message.toString(), LocalContext.current)
                        buttonClicked = false
                        loading = false
                    }
                    is Resource.Loading -> {
                        loading = true
                    }
                    is Resource.Success -> {
                        authViewModel.sendRecoverEmail(email, randomNumber.toString())
                        authViewModel.putString(Constants.KEY_EMAIL, email)
                        authViewModel.putString(Constants.KEY_OTP, randomNumber.toString())
                        buttonClicked = false
                        loading = false
                        authViewModel.putBoolean(Constants.KEY_RECOVER, true)

                        LaunchedEffect(Unit) {
                            navController.navigate(Verification.route) {
                                popUpTo(ResetPassword.route) { inclusive = true }
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


