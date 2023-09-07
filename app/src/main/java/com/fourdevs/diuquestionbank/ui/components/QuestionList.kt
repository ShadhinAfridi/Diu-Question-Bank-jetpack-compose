package com.fourdevs.diuquestionbank.ui.components

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.CardColors
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.fourdevs.diuquestionbank.ui.ads.AdmobBanner
import com.fourdevs.diuquestionbank.ui.authentication.showToast
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

    val context = LocalContext.current
    if(!userViewModel.checkInternetConnection()) {
        showToast(context, "No internet!")
    }

    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(navController = navController, name = department!!)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
        ) {
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
    var loading by rememberSaveable { mutableStateOf(true) }
    val questions = questionViewModel.questions?.collectAsLazyPagingItems()
    val scrollState = rememberLazyListState()
    var hasQuestionsLoaded by remember {
        mutableStateOf(false)
    }

    // Use LaunchedEffect to call the function only once
    LaunchedEffect(hasQuestionsLoaded) {
        if (!hasQuestionsLoaded) {
            questionViewModel.getQuestionsByCourse(departmentName, courseName!!, shift!!, exam!!)
            hasQuestionsLoaded = true
        }
    }

    LazyColumn(
        state = scrollState
    ) {

        questions?.itemCount?.let {itemCount ->
            items(itemCount) { count ->
                QuestionListItem(
                    navController = navController,
                    question = questions[count]!!,
                    questionViewModel = questionViewModel,
                    userViewModel = userViewModel
                )
                if ((count + 1) % 2 == 0 && count < itemCount - 1) {
                    ElevatedCard(
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ){
                        // Content of the card you want to insert
                        AdmobBanner(modifier = Modifier.fillMaxWidth(), adSize = AdSize.MEDIUM_RECTANGLE, userViewModel)
                    }
                }
            }

            when (questions.loadState.append) {
                is LoadState.Error -> {
                    item {
                        ErrorItem {
                            if(it) {
                                questionViewModel.getQuestionsByCourse(departmentName, courseName!!, shift!!, exam!!)
                            }
                        }
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
                        ErrorItem {
                            if(it) {
                                questionViewModel.getQuestionsByCourse(departmentName, courseName!!, shift!!, exam!!)
                            }
                        }
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

        if(questions?.itemCount==0 && !loading) {
            item {
                QuestionListItemWrapper(
                    onClick = {
                        questionViewModel.getQuestionsByCourse(departmentName, courseName!!, shift!!, exam!!)
                    }
                ){
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){

                        Text(
                            text = "No question found.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Reload",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun LoadingItem() {
    QuestionListItemWrapper(
        onClick = {}
    ){
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
fun ErrorItem(callback: (Boolean) -> Unit) {
    var clicked by remember {
        mutableStateOf(false)
    }
    QuestionListItemWrapper(
        onClick = {
            clicked = !clicked
            callback(clicked)
        },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.onError
            )
            Text(
                text = "Some error occurred",
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun QuestionListItem(
    navController: NavHostController,
    question: Question,
    questionViewModel: QuestionViewModel,
    userViewModel: UserViewModel
) {
    val downloadFlow = questionViewModel.questionDownloadFlow.collectAsState()
    var loading by remember { mutableStateOf(false) }
    var showPdf by remember { mutableStateOf(false) }

    questionViewModel.getUploaderName(question.uploaderId)

    val userResponse by questionViewModel.userResponseFlow.collectAsState()

    val currentUser = userResponse[question.uploaderId]
    val activity = LocalContext.current as Activity

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
                            userViewModel.showInterstitialAd(activity)
                            navController.navigate(PdfViewer.route + "/${question.link}/${question.courseName}")
                        }
                    }
                }
            }
        }
    }



    QuestionListItemWrapper(
        onClick = {
            questionViewModel.downloadFile(question.link)
            showPdf = true
        }
    ){
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionListItemWrapper(
    onClick: () -> Unit,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    content: @Composable (ColumnScope.() -> Unit)
) {
    AnimatedVisibility(true) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = onClick,
            colors = colors,
            content = content
        )
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
