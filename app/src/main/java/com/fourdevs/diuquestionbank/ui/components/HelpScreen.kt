package com.fourdevs.diuquestionbank.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
                .fillMaxSize()
                .padding(it)
        ) {
            HelpPage(userViewModel = userViewModel)
        }
    }
}

@Composable
fun HelpPage(
    userViewModel: UserViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var subject by remember{
            mutableStateOf("")
        }
        var message by remember{
            mutableStateOf("")
        }

        TextFiledWithUnderLine(
            label = "Name",
            value = userViewModel.getString(Constants.KEY_NAME)?: "",
            readonly = true,
            icon = Icons.Outlined.Email
        ) {
        }

        TextFiledWithUnderLine(
            label = "Email",
            value = userViewModel.getString(Constants.KEY_EMAIL)?: "",
            readonly = true,
            icon = Icons.Outlined.Email
        ) {
        }

        TextFiledWithUnderLine(
            label = "Subject",
            value = subject,
            readonly = false,
            icon = Icons.Outlined.Email
        ) {
            subject = it
        }

        TextFiledWithUnderLine(
            label = "Message",
            value = message,
            readonly = true,
            icon = Icons.Outlined.Email,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        ) {
            message = it
        }

        FilledTonalButton(
            onClick = {

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
