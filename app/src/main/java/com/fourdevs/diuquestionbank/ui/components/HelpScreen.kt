package com.fourdevs.diuquestionbank.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fourdevs.diuquestionbank.models.HelpRequest
import com.fourdevs.diuquestionbank.ui.authentication.showToast
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel

@Composable
fun HelpScreen(navController: NavController, userViewModel: UserViewModel) {
    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(navController = navController, name = "Help")
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            HelpPage(
                navController = navController,
                userViewModel = userViewModel
            )
        }
    }
}

@Composable
fun HelpPage(
    navController: NavController,
    userViewModel: UserViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var subject by rememberSaveable{
            mutableStateOf("")
        }
        var message by rememberSaveable{
            mutableStateOf("")
        }
        val context = LocalContext.current
        val userName by remember {
            mutableStateOf(userViewModel.getString(Constants.KEY_NAME)?: "")
        }
        val email by remember {
            mutableStateOf(userViewModel.getString(Constants.KEY_EMAIL)?: "")
        }
        val userId by remember {
            mutableStateOf(userViewModel.getString(Constants.KEY_USER_ID)?: "")
        }

        val addHelpResponse = userViewModel.addHelpFlow.collectAsState()

        TextFiledWithUnderLine(
            label = "Name",
            value = userName,
            readonly = true,
            icon = Icons.Outlined.Person
        ) {
        }

        TextFiledWithUnderLine(
            label = "Email",
            value = email,
            readonly = true,
            icon = Icons.Outlined.Email
        ) {
        }

        TextFiledWithUnderLine(
            label = "Subject",
            value = subject,
            readonly = false,
            icon = Icons.Outlined.Create
        ) {
            subject = it
        }


        TextFiledWithUnderLine(
            label = "Message",
            value = message,
            readonly = false,
            icon = Icons.Outlined.Edit,
            modifier = Modifier.height(150.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        ) { text ->
            message = text
        }

        FilledTonalButton(
            onClick = {
                if (subject.isEmpty() && message.isEmpty()) {
                    showToast(context, "All the fields are required.")
                    return@FilledTonalButton
                }
                val helpRequest = HelpRequest(
                    userId = userId,
                    userName = userName,
                    userEmail = email,
                    subject = subject,
                    message = message
                )

                if(!userViewModel.checkInternetConnection()) {
                    showToast(context, "You are offline!")
                    return@FilledTonalButton
                }
                userViewModel.addHelpRequest(helpRequest)
                if(addHelpResponse.value) {
                    showToast(context, "We have received your response.")
                    navController.popBackStack()
                } else {
                    showToast(context, "Some error occurred.")
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp),
            colors = ButtonDefaults
                .filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            shape = RoundedCornerShape(5.dp),
            content = {
                Text(
                    text = "Update",
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            },
            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)
        )
    }
}
