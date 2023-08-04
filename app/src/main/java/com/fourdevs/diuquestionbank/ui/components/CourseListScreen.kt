package com.fourdevs.diuquestionbank.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.data.Course
import com.fourdevs.diuquestionbank.data.archCourseList
import com.fourdevs.diuquestionbank.data.bsCourseList
import com.fourdevs.diuquestionbank.data.cisCourseList
import com.fourdevs.diuquestionbank.data.civilCourseList
import com.fourdevs.diuquestionbank.data.cseCourseList
import com.fourdevs.diuquestionbank.data.eCourseList
import com.fourdevs.diuquestionbank.data.eeeCourseList
import com.fourdevs.diuquestionbank.data.englishCourseList
import com.fourdevs.diuquestionbank.data.esdmCourseList
import com.fourdevs.diuquestionbank.data.getCourseList
import com.fourdevs.diuquestionbank.data.iceCourseList
import com.fourdevs.diuquestionbank.data.itmCourseList
import com.fourdevs.diuquestionbank.data.jmcCourseList
import com.fourdevs.diuquestionbank.data.lawCourseList
import com.fourdevs.diuquestionbank.data.mctCourseList
import com.fourdevs.diuquestionbank.data.nfeCourseList
import com.fourdevs.diuquestionbank.data.pessCourseList
import com.fourdevs.diuquestionbank.data.phCourseList
import com.fourdevs.diuquestionbank.data.pharmacyCourseList
import com.fourdevs.diuquestionbank.data.reCourseList
import com.fourdevs.diuquestionbank.data.sweCourseList
import com.fourdevs.diuquestionbank.data.teCourseList
import com.fourdevs.diuquestionbank.data.thmCourseList
import com.fourdevs.diuquestionbank.ui.navigation.QuestionList

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    screenName: String?,
    departmentName: String?,
    shift: String?,
    exam: String?,
    navController: NavHostController
) {



    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(navController = navController, name = "Select course name")
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyColumn {
                items(getCourseList(departmentName!!)) {

                    CourseListItem(
                        name = it.name,
                        department = departmentName,
                        shift = shift!!,
                        exam = exam!!,
                        screenName = screenName!!,
                        navController
                   )
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListItem(
    name: String,
    department: String,
    shift: String,
    exam: String,
    screenName: String,
    navController: NavHostController
) {

    val words = name.split(" ")
    var firstLetters = ""

    for (word in words) {
        if (word[0].isUpperCase()) {
            firstLetters += word[0]
        }
    }


    ElevatedCard(
        onClick = {
            navController.navigate( QuestionList.route+"/$department")
        },
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(48.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = if (firstLetters.length > 2) firstLetters.substring(
                            startIndex = 0,
                            endIndex = 3
                        ) else firstLetters,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = name,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "400 Questions",
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.End
                )

            }

        }


    }
}
