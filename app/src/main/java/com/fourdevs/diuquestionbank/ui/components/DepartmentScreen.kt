package com.fourdevs.diuquestionbank.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.ui.navigation.CourseList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentScreen(
    screenName: String?,
    departmentName: String?,
    navController: NavHostController
) {
    var shift by remember {
        mutableStateOf("")
    }
    var exam by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(navController, "$departmentName")
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.Start
            ) {
                RadioGroup(
                    "Shift",
                    listOf("Day", "Evening"),
                    shift
                ) { selectedText ->
                    shift = selectedText
                }

                RadioGroup(
                    stringResource(id = R.string.exam),
                    listOf(stringResource(id = R.string.midterm), stringResource(id = R.string.final_exam)),
                    exam
                ) { selectedText ->
                    exam = selectedText
                }
            }

            NextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(5.dp),
                navController = navController,
                departmentName = departmentName,
                shift = shift,
                exam = exam,
                context = context,
                screenName = screenName!!
            )
        }
    }

}

@Composable
fun RadioGroup(
    title: String,
    options: List<String>,
    selectedText: String,
    onSelectionChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )

        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )

        }

        Row(modifier = Modifier.fillMaxWidth()) {

            options.forEach { option ->
                Row(
                    Modifier
                        .wrapContentSize()
                        .height(56.dp)
                        .selectable(
                            selected = (option == selectedText),
                            onClick = { onSelectionChange(option) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (option == selectedText),
                        onClick = { onSelectionChange(option) }
                    )
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }

            }

        }
    }
}

@Composable
fun NextButton(
    modifier: Modifier,
    navController: NavHostController,
    departmentName: String?,
    shift: String,
    exam: String,
    context: Context,
    screenName:String
) {
    FilledTonalButton(
        onClick = {
            if(shift.isNotEmpty() && exam.isNotEmpty()) {
                navController.navigate(
                    CourseList.route+"/$screenName"+"/$departmentName"+"/$shift"+"/$exam"
                )
            } else {
                Toast.makeText(context, "Select all field!", Toast.LENGTH_LONG).show()
            }

        },
        modifier = modifier
        ,
        colors = ButtonDefaults
            .filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        shape = CutCornerShape(
            bottomEndPercent = 100,
            topEndPercent = 100
        ),
        content = {
            Text(
                text = "Next",
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        },
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)
    )
}

