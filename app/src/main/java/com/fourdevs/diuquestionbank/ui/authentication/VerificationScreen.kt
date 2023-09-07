package com.fourdevs.diuquestionbank.ui.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.ui.navigation.ChangePassword
import com.fourdevs.diuquestionbank.ui.navigation.LogIn
import com.fourdevs.diuquestionbank.ui.navigation.Verification
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VerificationScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    VerificationPage(navController, authViewModel)
}


@Composable
fun VerificationPage(navController: NavHostController, authViewModel: AuthViewModel) {
    val digitCount = 6
    var verificationCode by remember { mutableStateOf("      ") }
    var isTimerRunning by remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current
    var goToLogIn by remember { mutableStateOf(false) }
    var goToPasswordChange by remember { mutableStateOf(false) }
    var verify by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }
    val fromRecoveryPage = authViewModel.getBoolean(Constants.KEY_RECOVER)
    val context = LocalContext.current
    val networkFlow = authViewModel.networkResponseFlow.collectAsState()
    var loading by remember { mutableStateOf(false) }
    val countDownFlow = authViewModel.countDownFlow.collectAsState()

    AuthBackground(title = "Verify email") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Enter Verification Code", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(digitCount) { index ->
                    var digit by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = digit,
                        onValueChange = {
                            digit = it.take(1)
                            if (digit.isNotEmpty()) {
                                val charList = verificationCode.toMutableList()
                                if (index in 0 until charList.size) {
                                    charList[index] = digit[0]
                                    verificationCode = charList.joinToString(separator = "")
                                }
                                if(index<6) {
                                    localFocusManager.moveFocus(FocusDirection.Next)
                                }
                            } else {
                                if(index>0) {
                                    localFocusManager.moveFocus(FocusDirection.Previous)
                                }
                            }
                        },
                        modifier = Modifier
                            .width(48.dp)
                            .padding(0.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                TextButton(
                    onClick = {
                        goToLogIn=!goToLogIn
                    }
                ) {
                    Text(text = "Login")
                }

                TextButton(
                    enabled = enabled,
                    onClick = {
                        if(fromRecoveryPage) {
                            authViewModel.sendRecoverEmail(
                                authViewModel.getString(Constants.KEY_EMAIL)!!,
                                authViewModel.getString(Constants.KEY_OTP)!!
                            )
                        } else {
                            authViewModel.sentVerificationEmail(
                                authViewModel.getString(Constants.KEY_NAME)!!,
                                authViewModel.getString(Constants.KEY_EMAIL)!!,
                                authViewModel.getString(Constants.KEY_OTP)!!
                            )
                        }
                        authViewModel.startCountDown()
                        enabled = false
                        isTimerRunning = true
                    }
                ) {
                    Text(
                        text = if (isTimerRunning) countDownFlow.value else "Send Verification Code")
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryColorButton(label = "Verify") {
                if(authViewModel.checkInternetConnection()) {

                    loading = !loading
                    if(verificationCode == authViewModel.getString(Constants.KEY_OTP)) {
                        if(fromRecoveryPage) {
                            goToPasswordChange = true
                        } else {
                            loading = !loading
                            verify = !verify
                            authViewModel.verifyUser(authViewModel.currentUser!!.uid)
                        }
                    } else {
                        showToast(context, "Wrong otp code.")
                    }
                } else {
                    showNoInternet(context)
                }
            }
            
            if(goToLogIn) {
                authViewModel.putBoolean(Constants.KEY_IS_VERIFICATION_PAGE, false)
                LaunchedEffect(Unit) {
                    navController.navigate(LogIn.route) {
                        popUpTo(Verification.route) { inclusive = true }
                    }
                }
            }
            if(goToPasswordChange){
                LaunchedEffect(Unit) {
                    navController.navigate(ChangePassword.route) {
                        popUpTo(Verification.route) { inclusive = true }
                    }
                }
            }

            if(verify) {
                networkFlow.value?.let {
                    when(it) {
                        is Resource.Failure -> {
                            ShowToast(message = it.exception.message.toString(), LocalContext.current)
                            verify = !verify
                            loading = !loading
                        }
                        is Resource.Loading -> {
                            loading = true
                        }
                        is Resource.Success -> {
                            verify = !verify
                            loading = !loading
                            authViewModel.sendWelcomeEmail(
                                authViewModel.getString(Constants.KEY_NAME)!!,
                                authViewModel.getString(Constants.KEY_EMAIL)!!
                            )
                            goToLogIn = !goToLogIn
                        }
                    }
                }
            }
        }
    }

    if(countDownFlow.value=="Resend in 00:01") {
        enabled = true
        isTimerRunning = false
    }


    if(loading) {
        ProgressBar()
    }

}