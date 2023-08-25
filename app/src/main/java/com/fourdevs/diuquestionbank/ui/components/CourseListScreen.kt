package com.fourdevs.diuquestionbank.ui.components

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.data.Course
import com.fourdevs.diuquestionbank.data.getCourseList
import com.fourdevs.diuquestionbank.ui.ads.AdmobBanner
import com.fourdevs.diuquestionbank.ui.navigation.QuestionList
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel
import com.google.android.gms.ads.AdSize

@SuppressLint("UnrememberedMutableState")
@Composable
fun CourseListScreen(
    departmentName: String?,
    shift: String?,
    exam: String?,
    navController: NavHostController,
    questionViewModel: QuestionViewModel,
    userViewModel: UserViewModel
) {
    var courseList = emptyList<Course>()

    departmentName?.let {
        courseList = getCourseList(it)
    }

    var count = 0



    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(navController = navController, name = "Select course name")
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyColumn {

                items(courseList) {
                    count++
                    CourseListItem(
                        name = it.name,
                        department = departmentName!!,
                        shift = shift!!,
                        exam = exam!!,
                        navController,
                        questionViewModel
                    )

                    if ((count + 1) % 3 == 0 && count < courseList.size - 1) {
                        ElevatedCard(
                            modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        ){
                            // Content of the card you want to insert
                            Log.d("Afridi", "$count")
                            AdmobBanner(modifier = Modifier.fillMaxWidth(), adSize = AdSize.BANNER, userViewModel)
                        }
                    }

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
    navController: NavHostController,
    questionViewModel: QuestionViewModel
) {

    val words = name.split(" ")
    var firstLetters = ""

    for (word in words) {
        if (word[0].isUpperCase()) {
            firstLetters += word[0]
        }
    }

    questionViewModel.getQuestionCountByName(department, name, shift, exam)

    val courseCountState: State<Map<String, Int>> = questionViewModel.courseCountFlow.collectAsState(initial = emptyMap())

    val currentCourseCounts: Map<String, Int> = courseCountState.value

    val countText = currentCourseCounts[name]?.let { count ->
        if (count > 1) {
            "$count Questions"
        } else {
            "$count Question"
        }
    } ?: "Loading.."


    ElevatedCard(
        onClick = {
            navController.navigate(QuestionList.route + "/$department" + "/$name" + "/$shift" + "/$exam")
        },
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.background)
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
                        color = MaterialTheme.colorScheme.background,
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
                    text = countText,
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
