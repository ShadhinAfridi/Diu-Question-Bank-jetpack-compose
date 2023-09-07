package com.fourdevs.diuquestionbank.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.NavController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.departments
import com.fourdevs.diuquestionbank.data.getCourseList
import com.fourdevs.diuquestionbank.models.Notification
import com.fourdevs.diuquestionbank.ui.authentication.ErrorMessage
import com.fourdevs.diuquestionbank.ui.authentication.ShowToast
import com.fourdevs.diuquestionbank.ui.authentication.showToast
import com.fourdevs.diuquestionbank.viewmodel.NotificationViewModel
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Date

@Composable
fun UploadScreen(
    navController: NavController,
    questionViewModel: QuestionViewModel,
    notificationViewModel: NotificationViewModel,
    userViewModel: UserViewModel
) {
    AnimatedVisibility(
        visible = true,
        // Set the start width to 20 (pixels), 0 by default
        exit = shrinkHorizontally(
            // Overwrites the default animation with tween for this shrink animation.
            animationSpec = tween(),
            // Shrink towards the end (i.e. right edge for LTR, left edge for RTL). The default
            // direction for the shrink is towards [Alignment.Start]
            shrinkTowards = Alignment.End,
        ) { fullWidth ->
            // Set the end width for the shrink animation to a quarter of the full width.
            fullWidth / 4
        }
    ) {
        Upload(navController = navController, questionViewModel, notificationViewModel, userViewModel)
    }
}

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Upload(
    navController: NavController,
    questionViewModel: QuestionViewModel,
    notificationViewModel: NotificationViewModel,
    userViewModel: UserViewModel
) {
    val departmentList = emptyList<String>().toMutableList()
    departments.forEach { department ->
        departmentList += department.name
    }
    val exams = listOf("Midterm", "Final")
    val shifts = listOf("Day", "Evening")
    val semesters = listOf("Spring", "Summer", "Fall")
    val courseCodes = mutableListOf<String>()
    val courseNames = mutableListOf<String>()
    var departmentName by rememberSaveable { mutableStateOf("") }
    getCourseList(departmentName).forEach { course ->
        courseCodes.add(course.code)
        courseNames.add(course.name)
    }
    var courseCode by rememberSaveable { mutableStateOf("") }
    var courseName by rememberSaveable { mutableStateOf("") }
    var shiftName by rememberSaveable { mutableStateOf("") }
    var examName by rememberSaveable { mutableStateOf("") }
    var semesterName by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    var selectedFileUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                selectedFileUri = uri
            }
        }

    var clear by rememberSaveable {
        mutableStateOf(false)
    }
    var enabledButton by rememberSaveable {
        mutableStateOf(true)
    }
    val uploadComplete = questionViewModel.uploadCompleteFlow.collectAsState()
    val activity = LocalContext.current as Activity

    uploadComplete.value?.let {
        Log.d("Afridi", it.toString())

        if (it) {
            // Simulate upload process with a delay
            LaunchedEffect(true) {
                clear = true
                selectedFileUri = null
                notificationViewModel.showNotification(
                    Notification(
                        1,
                        "Complete",
                        "File Uploaded Successfully"
                    )
                )
                delay(200)
                enabledButton = true
                clear = false
            }
        }
    }




    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(navController = navController, name = "Upload Question")
        }
    ) { padding ->

        Column(
            modifier = Modifier.padding(padding)
        ) {


            ShowUploadProgress(questionViewModel = questionViewModel)

            UploadRow {
                DropDownField(
                    Modifier.fillMaxWidth(),
                    label = stringResource(id = R.string.department),
                    options = departmentList,
                    clear = clear
                ) {
                    departmentName = it
                }
            }

            UploadRow {
                DropDownField(
                    Modifier.fillMaxWidth(0.5f),
                    label = "Shift",
                    options = shifts,
                    clear = clear
                ) {
                    shiftName = it
                }
                Spacer(modifier = Modifier.padding(5.dp))
                DropDownField(
                    Modifier.fillMaxWidth(1f),
                    label = "Exam",
                    options = exams,
                    clear = clear
                ) {
                    examName = it
                }
            }

            UploadTextField(list = courseCodes, label = "Course code", clear = clear) {
                courseCode = it
            }

            UploadTextField(list = courseNames, label = "Course name", clear = clear) {
                courseName = it
            }


            UploadRow {
                DropDownField(
                    Modifier.fillMaxWidth(0.5f),
                    label = "Semester",
                    options = semesters,
                    clear = clear
                ) {
                    semesterName = it
                }
                Spacer(modifier = Modifier.padding(5.dp))
                DropDownField(
                    Modifier.fillMaxWidth(1f),
                    label = "Year",
                    options = yearList(),
                    clear = clear
                ) {
                    year = it
                }
            }


            OutlinedCard(
                onClick = {
                    if(userViewModel.checkPermissions()) {
                        launcher.launch(arrayOf("application/pdf"))
                    } else {
                        userViewModel.askPermission(activity)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                    .animateContentSize(),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onBackground)
            ) {
                UploadRow {
                    Icon(
                        painterResource(id = R.drawable.cloud_upload),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Select PDF", style = MaterialTheme.typography.bodyMedium)
                }
            }

            selectedFileUri?.let { uri ->

                val fileName =
                    DocumentFile.fromSingleUri(LocalContext.current, uri)?.name ?: "Unknown file"
                val fileSize = DocumentFile.fromSingleUri(LocalContext.current, uri)?.length() ?: 0
                val fileSizeKb = fileSize / 1024

                Text(
                    text = "$fileName -$fileSizeKb Kb",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }


            FilledTonalButton(
                onClick = {
                    selectedFileUri?.let { uri ->
                        enabledButton = false
                        if (departmentName.isNotEmpty() && shiftName.isNotEmpty() && examName.isNotEmpty() && courseCode.isNotEmpty() && courseName.isNotEmpty() && semesterName.isNotEmpty() && year.isNotEmpty() && selectedFileUri != null) {
                            val question = Question(
                                "",
                                courseCode,
                                courseName,
                                "",
                                departmentName,
                                shiftName,
                                examName,
                                semesterName,
                                year,
                                "",
                                "${Date()}",
                                0,
                                ""
                            )
                            questionViewModel.uploadFile(uri, question)
                        } else {
                            enabledButton = true
                            showToast(context, "All fields are required!")
                        }
                    }
                },
                enabled = enabledButton,
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
                        text = stringResource(id = R.string.upload),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    )
                },
                border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)
            )


        }
    }

}

@Composable
fun UploadRow(unit: @Composable (RowScope.() -> Unit)) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        content = unit
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownField(
    modifier: Modifier,
    label: String,
    options: List<String>,
    clear: Boolean,
    callback: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
    if (clear) {
        selectedOptionText = ""
    }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .focusProperties {
                    canFocus = false
                },
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        callback(selectedOptionText)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}


@Composable
fun yearList(): List<String> {
    val calendar = Calendar.getInstance()
    val dateList = ArrayList<String>()
    val currentYear = calendar.get(Calendar.YEAR)
    var addYear: String
    for (i in currentYear downTo 2015) {
        addYear = i.toString()
        dateList.add(addYear)
    }
    return dateList
}


@Composable
fun UploadTextField(
    list: List<String>,
    label: String,
    clear: Boolean,
    callback: (String) -> Unit
) {

    var textField by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(4, 20)))
    }
    if (clear) {
        textField = TextFieldValue(
            text = "", selection = TextRange(0)
        )
    }
    var isError by remember { mutableStateOf(false) }
    var focused by remember { mutableStateOf(false) }
    var dropDownList by remember {
        mutableStateOf(listOf<String>())
    }
    val localFocusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    OutlinedTextField(
        value = textField,
        onValueChange = {
            textField = it
            callback(it.text)
            dropDownList = list.filter { item ->
                item.contains(it.text, ignoreCase = true) && !item.equals(
                    it.text, ignoreCase = true
                )
            }
            isError = it.text.isEmpty()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    focused = true
                    if (textField.text.isEmpty()) isError = true
                } else focused = false
            },
        label = { Text(text = label) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            localFocusManager.moveFocus(FocusDirection.Down)
        }),
        textStyle = MaterialTheme.typography.bodyMedium,
        isError = isError,
        supportingText = {
            if (isError) {
                ErrorMessage(label = "This field is required")
            } else {
                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    items(dropDownList.size) {
                        dropDownList.forEach {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier
                                    .clickable(true) {
                                        textField = TextFieldValue(
                                            text = it, selection = TextRange(it.length)
                                        )
                                        dropDownList = mutableListOf()
                                    }
                                    .padding(5.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        })

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowUploadProgress(questionViewModel: QuestionViewModel) {
    val uploadProgress = questionViewModel.uploadProgressFlow.collectAsState()
    val uploadComplete = questionViewModel.uploadCompleteFlow.collectAsState()
    val uploadLoading = questionViewModel.uploadLoadingFlow.collectAsState()
    uploadComplete.value?.let {
        if (it) {
            ShowToast(message = "Uploaded successfully", context = LocalContext.current)
        } else {
            ShowToast(message = "Upload unsuccessful", context = LocalContext.current)
        }
    }


    if (uploadLoading.value) {
        AlertDialog(
            onDismissRequest = {}
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = uploadProgress.value ?: "Uploading..",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }


}