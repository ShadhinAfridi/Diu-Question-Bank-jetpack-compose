package com.fourdevs.diuquestionbank.ui.navigation

import android.annotation.SuppressLint
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.fourdevs.diuquestionbank.ui.authentication.ChangePasswordPage
import com.fourdevs.diuquestionbank.ui.authentication.LoginScreen
import com.fourdevs.diuquestionbank.ui.authentication.RecoverPassword
import com.fourdevs.diuquestionbank.ui.authentication.SignUpScreen
import com.fourdevs.diuquestionbank.ui.authentication.VerificationScreen
import com.fourdevs.diuquestionbank.ui.authentication.WelcomeScreen
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel

@SuppressLint("ComposableNavGraphInComposeScope")

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = Welcome.route,
        route = AuthNav.route
    ) {
        composable(Welcome.route) {
            WelcomeScreen(navController, authViewModel)
        }

        composable(LogIn.route) {
            LoginScreen(navController, authViewModel)
        }

        composable(ResetPassword.route) {
            RecoverPassword(navController, authViewModel)
        }

        composable(SignUp.route) {
            SignUpScreen(navController, authViewModel)
        }

        composable(Verification.route) {
            VerificationScreen(navController, authViewModel)
        }

        composable(ChangePassword.route) {
            ChangePasswordPage(navController, authViewModel)
        }

    }

}