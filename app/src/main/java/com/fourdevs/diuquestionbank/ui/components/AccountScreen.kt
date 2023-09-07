package com.fourdevs.diuquestionbank.ui.components

import android.graphics.Bitmap
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.ui.navigation.PdfViewer
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel


@Composable
fun AccountScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    questionViewModel: QuestionViewModel
) {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            UserCard(userViewModel = userViewModel)
            UserUpload(
                userViewModel = userViewModel,
                questionViewModel = questionViewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun UserCard(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    otherUserData: Boolean = false,
    userId: String = "",
    name: String = ""
) {
    val userInfoFlow = userViewModel.userInfoFlow.collectAsState()

    var about by remember {
        mutableStateOf("")
    }
    var image by remember {
        mutableStateOf<String?>(null)
    }
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    var uploadCount by remember {
        mutableIntStateOf(0)
    }
    var approvedCount by remember {
        mutableIntStateOf(0)
    }

    if (otherUserData) {
        userViewModel.getUserInfo(userId)
    } else {
        image = userViewModel.getString(Constants.KEY_USER_PROFILE_PIC)
    }

    image?.let {
        bitmap = userViewModel.bitmapFromEncodedString(it)
    }

    userInfoFlow.value?.let {
        about = it.about ?: "Loading.."
        image = it.image
        uploadCount = it.approvedCount + it.pendingCount + it.rejectedCount
        approvedCount = it.approvedCount
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .align(Alignment.TopStart),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    UserCardText(
                        text = if (otherUserData) name else userViewModel.getString(Constants.KEY_NAME)
                            ?: "Loading..",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                    UserCardText(
                        text = if (otherUserData) about else userViewModel.getString(Constants.KEY_USER_ABOUT)
                            ?: "Loading..",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .align(Alignment.BottomStart)
            ) {
                UserCardItem(
                    icon = R.drawable.ic_upload,
                    text = "Uploaded",
                    count = if (otherUserData) "$uploadCount" else userViewModel.getString(Constants.KEY_COUNT_UPLOAD)
                        ?: "0",
                    color = MaterialTheme.colorScheme.primary
                )
                UserCardItem(
                    icon = R.drawable.ic_done,
                    text = "Approved",
                    count = if (otherUserData) "$approvedCount" else userViewModel.getString(
                        Constants.KEY_COUNT_APPROVED
                    ) ?: "0",
                    color = MaterialTheme.colorScheme.tertiary
                )
                if (!otherUserData) {
                    UserCardItem(
                        icon = R.drawable.ic_hourglass,
                        text = "Pending",
                        count = userViewModel.getString(Constants.KEY_COUNT_PENDING) ?: "0",
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    UserCardItem(
                        icon = R.drawable.ic_close,
                        text = "Rejected",
                        count = userViewModel.getString(Constants.KEY_COUNT_REJECTED) ?: "0",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Image(
                painter = if (bitmap == null) painterResource(id = R.drawable.flower) else BitmapPainter(
                    bitmap?.asImageBitmap()!!
                ),
                contentDescription = "UserName",
                modifier = Modifier
                    .size(120.dp)
                    .clip(shape = CircleShape)
                    .align(Alignment.TopEnd),
                contentScale = ContentScale.Crop
            )
        }


    }
}

@Composable
fun UserCardText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    maxLines: Int
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines
    )
}

@Composable
fun UserCardItem(
    modifier: Modifier = Modifier,
    icon: Int,
    text: String,
    count: String,
    color: Color
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .wrapContentSize()
            .padding(top = 15.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = color,
            modifier = modifier.size(16.dp)
        )
        UserCardText(
            text = count,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1
        )
    }

}

@Composable
fun UserUpload(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    questionViewModel: QuestionViewModel,
    navController: NavHostController
) {
    val questions = userViewModel.questions.collectAsLazyPagingItems()

    // Use remember to remember whether the function has been called
    var hasQuestionsLoaded by remember {
        mutableStateOf(false)
    }

    // Use LaunchedEffect to call the function only once
    LaunchedEffect(hasQuestionsLoaded) {
        if (!hasQuestionsLoaded) {
            userViewModel.getString(Constants.KEY_USER_ID)?.let { userId ->
                userViewModel.getQuestionsByUser(userId)
                // Update the state to indicate that the function has been called
                hasQuestionsLoaded = true
            }
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxSize(),
        content = {
            items(questions.itemCount) { count ->
                questions[count]?.let { question ->
                    UserUploadCard(
                        question = question,
                        questionViewModel = questionViewModel,
                        navController = navController
                    )
                }
            }
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserUploadCard(
    modifier: Modifier = Modifier,
    question: Question,
    questionViewModel: QuestionViewModel,
    navController: NavHostController
) {
    var color = MaterialTheme.colorScheme.tertiary

    var icon by remember {
        mutableIntStateOf(R.drawable.ic_done)
    }
    if (question.isApproved == 0) {
        color = MaterialTheme.colorScheme.primary
        icon = R.drawable.ic_hourglass
    } else if (question.isApproved == 2) {
        color = MaterialTheme.colorScheme.error
        icon = R.drawable.ic_close
    }

    val downloadFlow = questionViewModel.questionDownloadFlow.collectAsState()
    var loading by remember { mutableStateOf(false) }
    var showPdf by remember { mutableStateOf(false) }

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

    ElevatedCard(
        modifier = modifier
            .padding(5.dp)
            .animateContentSize(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(5.dp),
        onClick = {
            questionViewModel.downloadFile(question.link)
            showPdf = true
        }

    ) {
        Box(modifier = modifier) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = modifier
                    .background(color = Color.Transparent)
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.TopStart)
            ) {
                UserCardText(
                    text = question.code,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
                UserCardText(
                    text = question.courseName,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 3
                )
                UserCardText(
                    text = question.departmentName,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3
                )
                UserCardText(
                    text = "${question.exam} ${question.semester} ${question.year}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
                UserCardText(
                    text = dateConverter(question.date),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }

            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Approved status",
                tint = color,
                modifier = modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp, bottom = 8.dp)
            )

            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }
    }
}