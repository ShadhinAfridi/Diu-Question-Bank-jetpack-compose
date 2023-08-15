package com.fourdevs.diuquestionbank.ui.components

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
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
import com.fourdevs.diuquestionbank.data.departments
import com.fourdevs.diuquestionbank.data.getCourseList
import com.fourdevs.diuquestionbank.ui.theme.LiteIconColor
import java.util.Calendar

@Composable
fun UploadScreen(navController: NavController, name: String?) {
    AnimatedVisibility(visible = true) {
        Upload(navController = navController, name = name)
    }
}

@SuppressLint("ResourceType", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Upload(navController: NavController, name: String?) {

    val localFocusManager = LocalFocusManager.current
    val departmentList = emptyList<String>().toMutableList()
    departments.forEach { department ->
        departmentList += department.name
    }
    val exams = listOf("Midterm", "Final")
    val shifts = listOf("Day", "Evening")
    val semesters = listOf("Spring", "Summer", "Fall")
    val courseCodes = mutableListOf<String>()
    val courseNames = mutableListOf<String>()
    var departmentName by remember { mutableStateOf("") }
    getCourseList(departmentName).forEach { course ->
        courseCodes.add(course.code)
        courseNames.add(course.name)
    }
    var courseCode by remember { mutableStateOf("") }
    var courseName by remember { mutableStateOf("") }
    var shiftName by remember { mutableStateOf("") }
    var examName by remember { mutableStateOf("") }
    var semesterName by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                selectedFileUri = uri
            }
        }


    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(navController = navController, name = "Upload $name")
        }
    ) { padding ->

        Column(
            modifier = Modifier.padding(padding)
        ) {

            UploadRow {
                departmentName = dropDownField(
                    Modifier.fillMaxWidth(),
                    label = stringResource(id = R.string.department),
                    options = departmentList
                )
            }

            UploadRow {
                shiftName =
                    dropDownField(Modifier.fillMaxWidth(0.5f), label = "Shift", options = shifts)
                Spacer(modifier = Modifier.padding(5.dp))
                examName = dropDownField(Modifier.fillMaxWidth(1f), label = "Exam", options = exams)
            }

            courseCode = uploadScreen(
                list = courseCodes,
                localFocusManager = localFocusManager,
                label = "Course code"
            )

            courseName = uploadScreen(
                list = courseNames,
                localFocusManager = localFocusManager,
                label = "Course name"
            )


            UploadRow {
                semesterName = dropDownField(
                    Modifier.fillMaxWidth(0.5f),
                    label = "Semester",
                    options = semesters
                )
                Spacer(modifier = Modifier.padding(5.dp))
                year =
                    dropDownField(Modifier.fillMaxWidth(1f), label = "Year", options = yearList())
            }


            OutlinedCard(
                onClick = { launcher.launch(arrayOf("application/pdf")) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, color = LiteIconColor)
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
                onClick = {},
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
                        color = Color.White,
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
fun dropDownField(modifier: Modifier, label: String, options: List<String>): String {

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
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
                unfocusedIndicatorColor = LiteIconColor,
                unfocusedLabelColor = LiteIconColor,
            ),

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
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }

    return selectedOptionText
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
fun uploadScreen(
    list: List<String>,
    localFocusManager: FocusManager,
    label: String
): String {

    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var dropDownList by remember {
        mutableStateOf(listOf<String>())
    }

    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it
            dropDownList = list.filter { item ->
                item.contains(it.text, ignoreCase = true) && !item.equals(
                    it.text, ignoreCase = true
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 10.dp),
        label = { Text(text = label) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            unfocusedIndicatorColor = LiteIconColor,
            unfocusedLabelColor = LiteIconColor,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
        ),
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardActions = KeyboardActions(onNext = {
            localFocusManager.moveFocus(FocusDirection.Down)
        }),
        supportingText = {
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
                                    value = TextFieldValue(
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
        })

    return value.text

}