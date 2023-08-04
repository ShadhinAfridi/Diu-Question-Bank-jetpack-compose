package com.fourdevs.diuquestionbank.ui.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.data.DepartmentData
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.departments
import com.fourdevs.diuquestionbank.ui.navigation.CourseList
import com.fourdevs.diuquestionbank.ui.navigation.Department
import com.fourdevs.diuquestionbank.ui.navigation.QuestionList
import com.fourdevs.diuquestionbank.ui.navigation.Upload
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun QuestionsScreen(
    navController: NavHostController,
    questionViewModel: QuestionViewModel
) {

    Scaffold(
        floatingActionButton = {
            IconButton(
                onClick = {
                    navController.navigate(
                        Upload.route + "/question"
                    )
                },
                modifier = Modifier.background(
                        MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add Questions",
                    modifier = Modifier.padding(5.dp),
                    tint = Color.White
                )
            }
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            Department("Question",navController)
        }
    }
}




@SuppressLint("ResourceType")
@Composable
fun Department(screenName:String, navController: NavHostController) {

    LazyColumn(

    ) {
        items(departments.size) {
            DepartmentItem(screenName, departments[it], navController)
        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentItem(screenName:String, department: DepartmentData, navController: NavHostController) {

    ElevatedCard(
        onClick = {
            navController.navigate( CourseList.route+"/${department}")
        },
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
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
                    text = "500 Questions",
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
