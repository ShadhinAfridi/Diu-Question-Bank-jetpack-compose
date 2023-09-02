package com.fourdevs.diuquestionbank.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.data.DepartmentData
import com.fourdevs.diuquestionbank.data.departments
import com.fourdevs.diuquestionbank.ui.ads.AdmobBanner
import com.fourdevs.diuquestionbank.ui.navigation.Department
import com.fourdevs.diuquestionbank.ui.navigation.Upload
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel
import com.google.android.gms.ads.AdSize


@Composable
fun QuestionsScreen(
    navController: NavHostController,
    questionViewModel: QuestionViewModel,
    userViewModel: UserViewModel
) {

    Scaffold(
        floatingActionButton = {
            IconButton(
                onClick = {
                    navController.navigate( Upload.route )
                }, modifier = Modifier.background(
                    MaterialTheme.colorScheme.primary, shape = CircleShape
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add Questions",
                    modifier = Modifier.padding(5.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            Department(navController, questionViewModel, userViewModel)
        }
    }
}


@SuppressLint("ResourceType")
@Composable
fun Department(
    navController: NavHostController,
    questionViewModel: QuestionViewModel,
    userViewModel: UserViewModel
) {
    
    LazyColumn {
        items(departments.size) { index ->
            DepartmentItem(departments[index], navController, questionViewModel)

            // Check if the current index is divisible by 3 and not the last item
            if ((index + 1) % 3 == 0 && index < departments.size - 1) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .animateContentSize(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ){
                    // Content of the card you want to insert
                    AdmobBanner(modifier = Modifier.fillMaxWidth(), adSize = AdSize.BANNER, userViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentItem(
    department: DepartmentData,
    navController: NavHostController,
    questionViewModel: QuestionViewModel
) {
    questionViewModel.getQuestionCountByDepartment(department.name)

    val departmentCountState: State<Map<String, Int>> =
        questionViewModel.departmentCountFlow.collectAsState(initial = emptyMap())

    val currentDepartmentCounts: Map<String, Int> = departmentCountState.value

    val countForDepartment: Int? = currentDepartmentCounts[department.name]

    var countText by rememberSaveable {
        mutableStateOf("")
    }

    countText = countForDepartment?.let { count ->
        if (count > 1) {
            "$count Questions"
        } else {
            "$count Question"
        }
    } ?: "Loading.."



    ElevatedCard(
        onClick = {
            navController.navigate(Department.route + "/${department.name}")
        },
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Icon(
                painterResource(id = R.drawable.school),
                contentDescription = department.name,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(10.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(
                    text = department.courseName,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
                Text(
                    text = department.name,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1
                )
                Text(
                    text = countText,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    textAlign = TextAlign.End,
                    maxLines = 1
                )
            }

            Icon(
                painterResource(id = R.drawable.arrow_forward),
                contentDescription = department.name,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(48.dp)
                    .padding(10.dp)
            )

        }

    }
}