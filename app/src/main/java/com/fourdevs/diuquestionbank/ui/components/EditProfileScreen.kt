package com.fourdevs.diuquestionbank.ui.components

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.ui.authentication.ShowToast
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel
import java.io.InputStream


@Composable
fun EditProfileScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,

    ) {
    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(navController = navController, name = "Edit profile")
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                EditProfile(userViewModel)
            }
        }
    )


}

@Composable
fun EditProfile(userViewModel: UserViewModel) {

    var department by remember {
        mutableStateOf(userViewModel.getString(Constants.KEY_USER_DEPARTMENT)?:"")
    }
    var about by remember {
        mutableStateOf(userViewModel.getString(Constants.KEY_USER_ABOUT)?:"")
    }
    var open by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val activity = context as Activity
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val userId = userViewModel.getString(Constants.KEY_USER_ID)!!
    val apiMessage = userViewModel.apiResponseFlow.collectAsState()
    val scrollState = rememberScrollState()

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                selectedFileUri = uri
                open = true
            }
        }

    if (open) {
        selectedFileUri?.let { uri ->
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                try {
                    // Decode the input stream into a Bitmap
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    // Update the ViewModel with the Bitmap data (if necessary)
                    val encodedString = userViewModel.bitmapToBase64(bitmap!!)
                    userViewModel.updateUserImage(userId, encodedString)
                    userViewModel.putString(Constants.KEY_USER_PROFILE_PIC, encodedString)
                    inputStream.close()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (apiMessage.value != null) {
                    ShowToast(message = apiMessage.value!!, context = context)
                    open = false
                }
            }
        }
    }




    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.TopCenter)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                userViewModel.getString(Constants.KEY_USER_PROFILE_PIC)?.let {
                    bitmap = userViewModel.bitmapFromEncodedString(it)
                }

                Box(
                    modifier = Modifier.wrapContentSize()
                ) {
                    Image(
                        painter = if (bitmap == null) painterResource(id = R.drawable.flower) else BitmapPainter(
                            bitmap?.asImageBitmap()!!
                        ),
                        contentDescription = "UserName",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(shape = CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = {
                            if(userViewModel.checkPermissions()) {
                                launcher.launch(arrayOf("image/*"))
                            } else {
                                userViewModel.askPermission(activity)
                            }

                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Add Image",
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
            }

            TextFiledWithUnderLine(
                label = "Name",
                value = userViewModel.getString(Constants.KEY_NAME)?: "",
                readonly = true,
                icon = Icons.Outlined.Person
            ) {
            }
            TextFiledWithUnderLine(
                label = "Email",
                value = userViewModel.getString(Constants.KEY_EMAIL)?: "",
                readonly = true,
                icon = Icons.Outlined.Email
            ) {
            }
            DropDownDepartment(department = department) { option ->
                department = option
            }
            TextFiledWithUnderLine(
                label = "About",
                value = about,
                readonly = false,
                icon = Icons.Outlined.Edit,
                modifier = Modifier.height(150.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            ) { text ->
                about = text
            }
        }
        FilledTonalButton(
            onClick = {
                if(department.isNotEmpty()) {
                    userViewModel.updateUserInfo(userId, department, about)
                    userViewModel.putString(Constants.KEY_USER_DEPARTMENT, department)
                    userViewModel.putString(Constants.KEY_USER_ABOUT, about)
                } else {
                    Toast.makeText(context, "Select department", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults
                .filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            shape = RoundedCornerShape(5.dp),
            content = {
                Text(
                    text = "Update",
                    color = Color.White,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            },
            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)
        )
    }

}


@Composable
fun TextFiledWithUnderLine(
    label: String,
    value: String,
    readonly: Boolean,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        label = { Text(text = label) },
        readOnly = readonly,
        leadingIcon = { Icon(imageVector = icon, contentDescription = label) },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedLabelColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        keyboardOptions = keyboardOptions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownDepartment(department: String, onValueChange: (String) -> Unit) {
    val departments = stringArrayResource(id = R.array.department_name).toList().distinct()

    var expanded by remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .wrapContentHeight(),
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            readOnly = true,
            value = department,
            onValueChange = {},
            label = { Text(text = "Department") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedLabelColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = "Department"
                )
            },


            )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            departments.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onValueChange(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}




