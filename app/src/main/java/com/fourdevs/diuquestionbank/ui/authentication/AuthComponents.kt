package com.fourdevs.diuquestionbank.ui.authentication

import android.content.Context
import android.content.res.Configuration
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.ui.theme.CircleColor
import com.fourdevs.diuquestionbank.ui.theme.LiteIconColor

@Preview(showBackground = true)
@Composable
fun ProgressBar() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .progressSemantics()
                .size(32.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun ShowToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}


@Composable
fun Circle(modifier: Modifier) {
    Box(
        modifier
            .clip(shape = RoundedCornerShape(100))
            .background(color = CircleColor)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun emailTextField(localFocusManager: FocusManager): String {

    var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(4, 20)))
    }
    var isError by remember { mutableStateOf(false) }
    var isValidEmail by remember { mutableStateOf(false) }
    val focusRequester = FocusRequester()

    TextField(
        value = email,
        onValueChange = {
            email = it
            isError = it.text.isEmpty()
            isValidEmail = it.text.isNotEmpty() && !isValidEmail(it.text)
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused && email.text.isEmpty()) isError = true
            },
        label = { Text(text = stringResource(id = R.string.email)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = stringResource(id = R.string.email),
                tint = LiteIconColor
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            unfocusedLabelColor = LiteIconColor,
            unfocusedIndicatorColor = LiteIconColor
        ),
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            localFocusManager.moveFocus(FocusDirection.Down)
        }),
        isError = isError || isValidEmail,
        supportingText = {
            if (isError) ErrorMessage(label = stringResource(id = R.string.required))
            if (isValidEmail) ErrorMessage(label = "Invalid email format")
        }

    )

    return email.text

}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun ErrorMessage(label: String) {
    Text(
        text = label,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.labelSmall
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textFieldPassword(
    label: String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
): String {

    var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(6, 20)))
    }
    val isVisibilityClicked = remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var lengthError by remember { mutableStateOf(false) }
    val focusRequester = FocusRequester()

    TextField(
        value = password,
        onValueChange = {
            password = it
            isError = it.text.isEmpty()
            lengthError = it.text.isNotEmpty() && it.text.length < 6
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused && password.text.isEmpty()) isError = true
            },
        label = { Text(text = label) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = stringResource(id = R.string.password),
                tint = LiteIconColor
            )
        },
        trailingIcon = {
            IconButton(onClick = { isVisibilityClicked.value = !isVisibilityClicked.value }) {
                Icon(
                    painterResource(id = if (isVisibilityClicked.value) R.drawable.visibility else R.drawable.visibility_off),
                    contentDescription = stringResource(id = R.string.password),
                    tint = if (isVisibilityClicked.value) MaterialTheme.colorScheme.primary else LiteIconColor
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            unfocusedLabelColor = LiteIconColor,
            unfocusedIndicatorColor = LiteIconColor,
        ),
        visualTransformation = if (isVisibilityClicked.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = isError || lengthError,
        supportingText = {
            if (isError) ErrorMessage(label = stringResource(id = R.string.required))
            if (lengthError) ErrorMessage(label = "Minimum 6 characters required")
        }

    )

    return password.text

}

@Composable
fun ButtonDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            color = LiteIconColor,
            thickness = 2.dp,
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
        )
        Text(
            text = stringResource(id = R.string.or),
            color = LiteIconColor,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Divider(
            color = LiteIconColor,
            thickness = 2.dp,
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
        )
    }
}

@Composable
fun PrimaryColorButton(label: String, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults
            .filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        shape = RoundedCornerShape(20.dp),
        content = {
            Text(
                text = label,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        },
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun BackgroundLessButton(label: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults
            .outlinedButtonColors(
                containerColor = Color.Transparent
            ),
        shape = RoundedCornerShape(20.dp),
        content = {
            Text(
                text = label,
                color = LiteIconColor,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        },
        border = BorderStroke(1.dp, color = LiteIconColor)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun nameTextField(localFocusManager: FocusManager): String {
    var userName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(4, 20)))
    }
    var isError by remember { mutableStateOf(false) }
    val focusRequester = FocusRequester()

    TextField(
        value = userName,
        onValueChange = {
            userName = it
            isError = it.text.isEmpty()
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) isError = true
            },
        label = { Text(text = stringResource(id = R.string.name)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = stringResource(id = R.string.name),
                tint = LiteIconColor
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            unfocusedLabelColor = LiteIconColor,
            unfocusedIndicatorColor = LiteIconColor
        ),
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            localFocusManager.moveFocus(FocusDirection.Down)
        }),
        isError = isError,
        supportingText = {
            if (isError) ErrorMessage(label = stringResource(id = R.string.required))
        }


    )

    return userName.text
}

@Composable
fun AuthBackground(title: String, design: @Composable (ColumnScope.() -> Unit)) {

    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {

        }

        else -> {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                        .background(
                            color = MaterialTheme.colorScheme.primary
                        )
                ) {

                    Circle(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopEnd)
                    )

                    Circle(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.BottomCenter)
                    )

                    Text(
                        text = title,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(20.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center
                    )

                }
                Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.primary)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(topStart = 100.dp)
                            )
                            .padding(20.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Center,
                            content = design
                        )

                    }
                }

            }

        }
    }
}

fun showNoInternet(context : Context) {
    Toast.makeText(context, "Please check your internet connection and try again.", Toast.LENGTH_SHORT).show()
}
fun showToast(context : Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}