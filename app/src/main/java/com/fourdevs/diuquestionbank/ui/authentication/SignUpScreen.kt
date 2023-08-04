package com.fourdevs.diuquestionbank.ui.authentication

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.ui.navigation.LogIn
import com.fourdevs.diuquestionbank.ui.navigation.SignUp
import com.fourdevs.diuquestionbank.ui.navigation.Verification
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseUser


@Composable
fun SignUpScreen(navController: NavHostController, viewModel: AuthViewModel) {

    SignUp(navController, viewModel)

}

@Composable
fun SignUp(navController: NavHostController, viewModel: AuthViewModel) {

    var email by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val authResource = viewModel.signupFlow.collectAsState()
    val context = LocalContext.current
    val randomNumber = viewModel.generateRandomNumber()
    val localFocusManager = LocalFocusManager.current
    var goToLogIn by remember { mutableStateOf(false) }
    var buttonClicked by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }


    AuthBackground(stringResource(id = R.string.sign_up))  {
        userName = nameTextField(localFocusManager = localFocusManager)
        Spacer(modifier = Modifier.height(20.dp))
        email = emailTextField(localFocusManager = localFocusManager)
        Spacer(modifier = Modifier.height(20.dp))
        password = textFieldPassword(
            label = stringResource(id = R.string.password),
            keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                localFocusManager.moveFocus(FocusDirection.Down)
            })
        )
        Spacer(modifier = Modifier.height(20.dp))

        confirmPassword = textFieldPassword(
            label = stringResource(id = R.string.confirm_password),
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


        Spacer(modifier = Modifier.height(30.dp))


        PrimaryColorButton(label = stringResource(id = R.string.sign_up)) {
            if(viewModel.checkInternetConnection()) {
                if(userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && isValidEmail(email)) {
                    if(!checkDiuEmail(email)) {
                        if(password.length>=6 && confirmPassword.length>=6 && password==confirmPassword) {
                            viewModel.signupUser(userName, email, password)
                            loading = true
                            buttonClicked = !buttonClicked
                        } else {
                            showToast(context, "Password don't match!")
                        }
                    } else {
                        showNoInternet(context)
                    }
                }
            } else {
                showNoInternet(context)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        ButtonDivider()

        Spacer(modifier = Modifier.height(10.dp))

        BackgroundLessButton(label = stringResource(id = R.string.log_in)) {
            goToLogIn = true
        }

        if(buttonClicked) {
            authResource.value?.let {
                when (it) {
                    is Resource.Failure -> {
                        ShowToast(message = it.exception.message.toString(), LocalContext.current)
                        buttonClicked = !buttonClicked
                        loading = false
                    }
                    is Resource.Loading -> {
                        loading = true
                    }
                    is Resource.Success -> {
                        loading = !loading
                        buttonClicked = !buttonClicked
                        viewModel.sentVerificationEmail(userName, email, randomNumber.toString())
                        GetUserData(
                            user = viewModel.currentUser,
                            viewModel = viewModel,
                            navController = navController,
                            otp = randomNumber.toString()
                        )
                    }
                }
            }
        }

        if(goToLogIn) {
            LaunchedEffect(Unit) {
                navController.navigate(LogIn.route) {
                    popUpTo(SignUp.route) { inclusive = true }
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
    navController: NavHostController,
    otp : String
) {
    user?.let {
        val name = it.displayName
        val email = it.email
        val uid = it.uid
        viewModel.putBoolean(Constants.KEY_IS_VERIFICATION_PAGE, true)
        viewModel.putString(Constants.KEY_NAME, name!!)
        viewModel.putString(Constants.KEY_EMAIL, email!!)
        viewModel.putString(Constants.KEY_USER_ID, uid)
        viewModel.putString(Constants.KEY_OTP, otp)
        GoForVerification(navController)
    }
}

@Composable
private fun GoForVerification(navController: NavHostController) {
    LaunchedEffect(Unit) {
        navController.navigate(Verification.route) {
            popUpTo(SignUp.route) { inclusive = true }
        }
    }
}

fun checkDiuEmail(email: String):Boolean {
    val regex1 = Regex("[a-zA-Z0-9._-]+@diu.edu.bd")
    val regex2 = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+diu.edu.bd")
    return email.matches(regex1) || email.matches(regex2)
}