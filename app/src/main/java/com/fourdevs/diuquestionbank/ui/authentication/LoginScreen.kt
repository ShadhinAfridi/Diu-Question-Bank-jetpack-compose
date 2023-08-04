package com.fourdevs.diuquestionbank.ui.authentication

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.ui.navigation.Home
import com.fourdevs.diuquestionbank.ui.navigation.LogIn
import com.fourdevs.diuquestionbank.ui.navigation.ResetPassword
import com.fourdevs.diuquestionbank.ui.navigation.SignUp
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseUser


@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    LogIn(navController, viewModel)
}

@Composable
private fun LogIn(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var logInClicked by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    val authResource = viewModel.loginFlow.collectAsState()
    val localFocusManager = LocalFocusManager.current
    val context = LocalContext.current


    AuthBackground(stringResource(id = R.string.log_in)) {
        email = emailTextField(localFocusManager = localFocusManager)
        Spacer(modifier = Modifier.height(20.dp))
        password = textFieldPassword(
            label = stringResource(id = R.string.password),
            keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions =
            KeyboardActions(onNext = {
                localFocusManager.clearFocus(true)
            })
        )

        TextButton(
            onClick = {
                navController.navigate(
                    ResetPassword.route
                )
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = stringResource(id = R.string.forgotten_password),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        PrimaryColorButton(label = stringResource(id = R.string.log_in)) {
            if(viewModel.checkInternetConnection()) {
                if(password.length>=6 && isValidEmail(email)) {
                    viewModel.loginUser(email, password)
                    logInClicked = true
                    loading = true
                }
            } else {
                showNoInternet(context)
            }

        }

        Spacer(modifier = Modifier.height(10.dp))

        ButtonDivider()

        Spacer(modifier = Modifier.height(10.dp))

        BackgroundLessButton(label = stringResource(id = R.string.sign_up)) {
            navController.navigate(
                SignUp.route
            )
        }

        if(logInClicked) {
            authResource.value?.let {
                when (it) {
                    is Resource.Failure -> {
                        ShowToast(message = it.exception.message.toString(), LocalContext.current)
                        logInClicked=!logInClicked
                        loading = false
                    }
                    is Resource.Loading -> {
                        loading = true
                    }
                    is Resource.Success -> {
                        logInClicked=!logInClicked
                        loading = false
                        val user = viewModel.currentUser
                        GetUserData(user, viewModel, navController)
                    }
                }
            }
        }


    }

    if(loading) {
        ProgressBar()
    }

}


@Composable
private fun GetUserData(
    user: FirebaseUser?,
    viewModel: AuthViewModel,
    navController: NavHostController
) {

    user?.let {
        val name = it.displayName
        val email = it.email
        val photoUrl = it.photoUrl
        val emailVerified = it.isEmailVerified
        val uid = it.uid

        if(emailVerified) {
            viewModel.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
            viewModel.putString(Constants.KEY_NAME, name!!)
            viewModel.putString(Constants.KEY_EMAIL, email!!)
            viewModel.putString(Constants.KEY_USER_PROFILE_PIC, photoUrl.toString())
            viewModel.putBoolean(Constants.KEY_IS_VERIFIED, true)
            viewModel.putString(Constants.KEY_USER_ID, uid)
            CompleteLogIn(navController)
            viewModel.getIdToken(user)

        } else {
            ShowToast(message = "User not verified!", context = LocalContext.current)
            viewModel.logout()
        }
    }



}

@Composable
private fun CompleteLogIn(navController: NavHostController) {

    LaunchedEffect(Unit) {
        navController.navigate(Home.route) {
            popUpTo(LogIn.route) { inclusive = true }
        }
    }
    ShowToast(message = "Login successful!", context = LocalContext.current)
}