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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import com.fourdevs.diuquestionbank.utilities.Constants

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
fun AuthTextField(
    label: String,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions,
    onValueChange: (String) -> Unit
) {
    var textField by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(4, 20)))
    }
    val focusRequester = FocusRequester()
    var isError by remember { mutableStateOf(false) }
    var icon by remember {
        mutableStateOf(Icons.Default.Person)
    }
    var keyboardType by remember {
        mutableStateOf(KeyboardType.Email)
    }

    var errorMessage by remember {
        mutableStateOf("This field is required")
    }
    var isVisibilityClicked by remember { mutableStateOf(false) }

    var focused by remember { mutableStateOf(false) }

    when (label) {
        Constants.DATA_NAME -> {
            icon = Icons.Outlined.Person
        }

        Constants.DATA_EMAIL -> {
            icon = Icons.Outlined.Email
            keyboardType = KeyboardType.Email
            if (!isValidEmail(textField.text) && textField.text.isNotEmpty()) {
                errorMessage = "Invalid email format"
                isError = true
            }
        }

        Constants.DATA_PASSWORD, Constants.DATA_CONFIRM_PASSWORD -> {
            icon = Icons.Outlined.Lock
            keyboardType = KeyboardType.Password
            if (textField.text.length in 1..5) {
                errorMessage = "Minimum 6 characters required"
                isError = true
            }
        }



    }

    TextField(
        value = textField,
        onValueChange = {
            textField = it
            onValueChange(it.text)
            isError = it.text.isEmpty()
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if(focusState.isFocused) {
                    focused = true
                    if (textField.text.isEmpty()) isError = true
                } else focused = false
            },
        label = { Text(text = label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isError) {
                    MaterialTheme.colorScheme.error
                } else if(focused) {
                    MaterialTheme.colorScheme.primary
                } else MaterialTheme.colorScheme.onBackground
            )
        },
        trailingIcon = {
            if (label == Constants.DATA_PASSWORD || label == Constants.DATA_CONFIRM_PASSWORD) {
                IconButton(onClick = { isVisibilityClicked = !isVisibilityClicked }) {
                    Icon(
                        painterResource(id = if (isVisibilityClicked) R.drawable.visibility else R.drawable.visibility_off),
                        contentDescription = stringResource(id = R.string.password),
                        tint = if (isVisibilityClicked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        visualTransformation = if (label == Constants.DATA_PASSWORD || label == Constants.DATA_CONFIRM_PASSWORD) {
            if (isVisibilityClicked) VisualTransformation.None else PasswordVisualTransformation()
        } else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType, imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        isError = isError,
        supportingText = {
            if (isError) ErrorMessage(label = errorMessage)
        }

    )

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
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 2.dp,
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
        )
        Text(
            text = stringResource(id = R.string.or),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Divider(
            color = MaterialTheme.colorScheme.onBackground,
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
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        shape = RoundedCornerShape(20.dp),
        content = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onPrimary,
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
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(20.dp),
        content = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        },
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onBackground)
    )
}


@Composable
fun AuthBackground(title: String, design: @Composable (ColumnScope.() -> Unit)) {

    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {

        }

        else -> {

            Column(
                modifier = Modifier.fillMaxSize(),
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

                    Text(
                        text = title,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
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

fun showNoInternet(context: Context) {
    Toast.makeText(
        context, "Please check your internet connection and try again.", Toast.LENGTH_SHORT
    ).show()
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}