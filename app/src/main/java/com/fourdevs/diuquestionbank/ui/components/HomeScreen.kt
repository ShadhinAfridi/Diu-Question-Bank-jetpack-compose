package com.fourdevs.diuquestionbank.ui.components

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.ui.ads.AdmobBanner
import com.fourdevs.diuquestionbank.ui.authentication.showToast
import com.fourdevs.diuquestionbank.ui.navigation.Department
import com.fourdevs.diuquestionbank.ui.navigation.EditProfileScreen
import com.fourdevs.diuquestionbank.ui.navigation.Upload
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.NotificationViewModel
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel
import com.google.android.gms.ads.AdSize

@Composable
fun HomeScreen(
    navController: NavHostController,
    questionViewModel: QuestionViewModel,
    userViewModel: UserViewModel,
    notificationViewModel: NotificationViewModel
) {
    val activity = LocalContext.current as Activity
    val scrollState = rememberLazyListState()
    var notificationAllowed by rememberSaveable {
        mutableStateOf(false)
    }
    var didOnce by rememberSaveable {
        mutableStateOf(false)
    }

    if(!didOnce) {
        notificationAllowed = notificationViewModel.checkNotificationPermission()
        didOnce = true
    }

    if(!notificationAllowed) {
        notificationViewModel.askNotificationPermission(activity)
    }

    Scaffold{
        Box(modifier = Modifier.padding(it)){
            userViewModel.getString(Constants.KEY_USER_ID)?.let {userId->
                userViewModel.getQuestionsByUser(userId)
            }
            Home(
                navController = navController,
                userViewModel = userViewModel,
                questionViewModel = questionViewModel,
                scrollState = scrollState
            )
        }
    }
}

@Composable
fun Home(
    navController: NavHostController,
    userViewModel: UserViewModel,
    questionViewModel: QuestionViewModel,
    scrollState: LazyListState
) {
    val context = LocalContext.current
    val activity = context as Activity
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        HomeCard {
            SearchBarDesign()
        }
        HomeCard {
            AdmobBanner(modifier = Modifier.fillMaxWidth(), adSize = AdSize.BANNER, userViewModel)
        }
        HomeOptionCard(navController = navController, userViewModel = userViewModel)
        HomeCard {
            AdmobBanner(modifier = Modifier.fillMaxWidth(), adSize = AdSize.BANNER, userViewModel)
        }

        YourUploadCard(navController, questionViewModel, userViewModel, scrollState)
        HomeCard {
            AdmobBanner(modifier = Modifier.fillMaxWidth(), adSize = AdSize.BANNER, userViewModel)
        }
        Button(onClick = { userViewModel.showInterstitialAd(activity) }) {
            Text(text = "Show")
        }
    }

}

@Composable
fun HomeOptionCard(navController: NavHostController, userViewModel: UserViewModel) {
    val context = LocalContext.current

    HomeCard {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            item {
                IconCard(
                    title = "Questions",
                    icon = R.drawable.questions,
                    color = Color.Magenta
                ) {
                    val department = userViewModel.getString(Constants.KEY_USER_DEPARTMENT)

                    if (department != null) {
                        navController.navigate(Department.route + "/$department")
                    } else {
                        showToast(context, "Please update your department.")
                    }
                }
            }
            item {
                IconCard(
                    title = "Upload",
                    icon = R.drawable.upload,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    navController.navigate(Upload.route)
                }
            }
            item {
                IconCard(
                    title = "Search",
                    icon = R.drawable.ic_search,
                    color = Color.Cyan
                ) {

                }
            }
            item {
                IconCard(
                    title = "Account",
                    icon = R.drawable.person,
                    color = Color.Green
                ) {
                    navController.navigate(EditProfileScreen.route)
                }
            }
            item {
                IconCard(
                    title = "Setting",
                    icon = R.drawable.settings,
                    color = Color.DarkGray
                ) {


                }
            }
            item {
                IconCard(
                    title = "Help",
                    icon = R.drawable.ic_help,
                    color = Color.Blue
                ) {

                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconCard(
    title: String,
    icon: Int,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(5.dp)
            .wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Icon(
                painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier
                    .size(24.dp),
                tint = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarDesign() {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(bottom = 5.dp)
            .semantics { }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { },
            query = text,
            onQueryChange = { text = it },
            onSearch = { active = false },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text("Search for question")
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                IconButton(onClick = { active = !active }) {
                    if (active) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    } else {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
                    }
                }
            }
        ) {
            repeat(4) { idx ->
                val resultText = "Suggestion $idx"
                ListItem(
                    headlineContent = { Text(resultText) },
                    supportingContent = { Text("Additional info") },
                    leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                    modifier = Modifier
                        .clickable {
                            text = resultText
                            active = false
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

    }

}


@Composable
fun YourUploadCard(
    navController: NavHostController,
    questionViewModel: QuestionViewModel,
    userViewModel: UserViewModel,
    scrollState: LazyListState
) {
    val questions = userViewModel.questions?.collectAsLazyPagingItems()

    HomeCard {
        Text(
            text = "Your Contribution",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(10.dp)
        )
        LazyRow(
            state = scrollState,
            modifier = Modifier.padding(5.dp)
        ) {
            questions?.itemCount?.let {
                items(it) { count ->
                    UserUploadCard(
                        navController = navController,
                        question = questions[count]!!,
                        questionViewModel = questionViewModel
                    )
                }

            }
        }
    }
}

@Composable
fun HomeCard(content: @Composable (ColumnScope.() -> Unit)) {
    AnimatedVisibility(true) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .animateContentSize()
                .padding(5.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            content = content
        )
    }
}
