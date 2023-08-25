package com.fourdevs.diuquestionbank.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.data.departments
import com.fourdevs.diuquestionbank.ui.ads.AdmobBanner
import com.fourdevs.diuquestionbank.ui.navigation.PdfViewer
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel
import com.google.android.gms.ads.AdSize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun QuestionList(
    department: String?,
    courseName: String?,
    shift: String?,
    exam: String?,
    navController: NavHostController,
    questionViewModel: QuestionViewModel,
    userViewModel: UserViewModel
) {

    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(navController = navController, name = department!!)
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            GetData(
                navController = navController,
                departmentName = department!!,
                courseName = courseName,
                shift = shift,
                exam = exam,
                questionViewModel = questionViewModel,
                userViewModel = userViewModel
            )
        }
    }

}

@Composable
private fun GetData(
    navController: NavHostController,
    departmentName: String,
    courseName: String?,
    shift: String?,
    exam: String?,
    questionViewModel: QuestionViewModel,
    userViewModel: UserViewModel
) {
    var loading by remember { mutableStateOf(false) }
    val questions = questionViewModel.questions?.collectAsLazyPagingItems()
    val scrollState = rememberLazyListState()

    LaunchedEffect(scrollState) {
        questionViewModel.getQuestionsByCourse(departmentName, courseName!!, shift!!, exam!!)
    }

    LazyColumn(state = scrollState) {

        questions?.itemCount?.let {itemCount ->
            items(itemCount) { count ->
                QuestionListItem(
                    navController = navController,
                    question = questions[count]!!,
                    questionViewModel = questionViewModel
                )
                if ((count + 1) % 2 == 0 && count < itemCount - 1) {
                    ElevatedCard(
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ){
                        // Content of the card you want to insert
                        Log.d("Afridi", "$count")
                        AdmobBanner(modifier = Modifier.fillMaxWidth(), adSize = AdSize.MEDIUM_RECTANGLE, userViewModel)
                    }
                }
            }

            when (questions.loadState.append) {
                is LoadState.Error -> {
                    item {
                        ErrorItem()
                    }
                }

                LoadState.Loading -> {
                    loading = true
                    item {
                        LoadingItem()
                    }
                }

                is LoadState.NotLoading -> {
                    loading = false
                }
            }

            when (questions.loadState.refresh) {
                is LoadState.Error -> {
                    item {
                        ErrorItem()
                    }
                }

                LoadState.Loading -> {
                    loading = true
                    item {
                        LoadingItem()
                    }
                }

                is LoadState.NotLoading -> {
                    loading = false
                }
            }
        }

    }


}

@Composable
fun LoadingItem() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(42.dp)
                    .padding(8.dp),
                strokeWidth = 5.dp
            )
        }
    }
}

@Composable
fun ErrorItem() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Error",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background)
            )
            Text(text = "Some error occurred", color = MaterialTheme.colorScheme.background)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionListItem(
    navController: NavHostController,
    question: Question,
    questionViewModel: QuestionViewModel
) {
    val downloadFlow = questionViewModel.questionDownloadFlow.collectAsState()
    var loading by remember { mutableStateOf(false) }
    var showPdf by remember { mutableStateOf(false) }

    questionViewModel.getUploaderName(question.uploaderId)

    val userResponse by questionViewModel.userResponseFlow.collectAsState()

    val currentUser = userResponse[question.uploaderId]

    if (showPdf) {
        downloadFlow.value?.let {
            when (it) {
                is Resource.Loading -> {
                    loading = true
                }

                is Resource.Failure -> {
                    loading = false
                    it.exception.printStackTrace()
                }

                is Resource.Success -> {
                    loading = false
                    if (it.result == 100.0) {
                        LaunchedEffect(true) {
                            navController.navigate(PdfViewer.route + "/${question.link}/${question.questionId}")
                        }
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            questionViewModel.downloadFile(question.link)
            showPdf = true
        },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = question.code,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${question.semester} ${question.year}",
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = question.courseName,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = question.exam,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.flower),
                        contentDescription = "Uploader Image",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop

                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currentUser?.displayName ?: "Loading...",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = "Upload Date",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = dateConverter(question.date),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.wrapContentWidth(Alignment.End),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }
    }

}


fun dateConverter(dateString: String): String {
    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

    return try {
        val date: Date = inputFormat.parse(dateString) as Date
        outputFormat.format(date)

    } catch (e: Exception) {
        e.printStackTrace()
        return e.message!!
    }
}
