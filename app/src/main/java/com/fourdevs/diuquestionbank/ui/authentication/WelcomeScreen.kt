package com.fourdevs.diuquestionbank.ui.authentication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.ui.navigation.LogIn
import com.fourdevs.diuquestionbank.ui.navigation.SignUp
import com.fourdevs.diuquestionbank.ui.navigation.Verification
import com.fourdevs.diuquestionbank.ui.navigation.Welcome
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel

@Composable
fun WelcomeScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    Welcome(navController)

    if(authViewModel.getBoolean(Constants.KEY_IS_VERIFICATION_PAGE)) {
        LaunchedEffect(Unit) {
            navController.navigate(Verification.route) {
                popUpTo(Welcome.route) { inclusive = true }
            }
        }
    } else {
        authViewModel.logout()
    }

}


@Composable
fun Welcome(navController: NavHostController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),

        ) {

        Circle(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterEnd)
        )

        Circle(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopStart)
        )

        Circle(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.BottomStart)
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Bottom
        ) {

            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.app_intro),
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Start
            )


            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate(
                        LogIn.route
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults
                    .outlinedButtonColors(
                        containerColor = Color.White
                    ),
                shape = RoundedCornerShape(20.dp),
                content = {
                    Text(
                        text = stringResource(id = R.string.log_in),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                },
                border = BorderStroke(1.dp, color = Color.White)
            )

            Spacer(modifier = Modifier.height(20.dp))

            FilledTonalButton(
                onClick = {
                    navController.navigate(
                        SignUp.route
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults
                    .filledTonalButtonColors(
                        containerColor = Color.Transparent

                    ),
                shape = RoundedCornerShape(20.dp),
                content = {
                    Text(
                        text = stringResource(id = R.string.sign_up),
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                },
                border = BorderStroke(1.dp, color = Color.White),
            )

        }

    }

}