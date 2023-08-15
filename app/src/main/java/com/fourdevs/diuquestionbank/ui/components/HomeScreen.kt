package com.fourdevs.diuquestionbank.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.utilities.Constants

@Composable
fun HomeScreen(navController: NavHostController) {

//    val userInfo = viewModel.userInfoFlow.collectAsState()
//    viewModel.getUserInfo(uid)
//    userInfo.value?.let {userInfo->
//        viewModel.putString(Constants.KEY_USER_DEPARTMENT, userInfo.department ?: "none")
//        viewModel.putString(Constants.KEY_USER_ABOUT, userInfo.about ?: "none")
//        viewModel.putString(Constants.KEY_USER_PROFILE_PIC, userInfo.image ?: "none")
//    }

    Home()

}

@Preview(showBackground = true)
@Composable
fun Home() {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 5.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            item {
                IconCard(
                    onClick = { /*TODO*/ },
                    title = "Questions",
                    icon = R.drawable.questions,
                    color = Color.Magenta
                )
            }
            item {
                IconCard(
                    onClick = { /*TODO*/ },
                    title = "Upload",
                    icon = R.drawable.upload,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                IconCard(
                    onClick = { /*TODO*/ },
                    title = "Search",
                    icon = R.drawable.ic_search,
                    color = Color.Cyan
                )
            }
            item {
                IconCard(
                    onClick = { /*TODO*/ },
                    title = "Account",
                    icon = R.drawable.person,
                    color = Color.Green
                )
            }
            item {
                IconCard(
                    onClick = { /*TODO*/ },
                    title = "Help",
                    icon = R.drawable.ic_help,
                    color = Color.Blue
                )
            }

        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconCard(
    onClick: () -> Unit,
    title: String,
    icon: Int,
    color: Color
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(5.dp)
            .wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
