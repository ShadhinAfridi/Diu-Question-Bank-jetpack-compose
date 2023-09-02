package com.fourdevs.diuquestionbank.ui.components

import android.graphics.Bitmap
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.ui.navigation.EditProfileScreen
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(name: String) {
    TopAppBar(
        title = {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
        },
        colors = TopAppBarDefaults
            .mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithBackIcon(
    navController: NavController,
    name: String
) {
    TopAppBar(
        title = {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults
            .mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )

    )
}


@Composable
fun AccountTopAppBar(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .animateContentSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        HomeAppBar(viewModel)
        OutlinedButton(
            onClick = {
                navController.navigate(EditProfileScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(text = "Edit your profile", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(viewModel: AuthViewModel) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    viewModel.getString(Constants.KEY_USER_PROFILE_PIC)?.let {
        bitmap = viewModel.bitmapFromEncodedString(it)
    }
    TopAppBar(
        title = {
            Column(modifier = Modifier.padding(horizontal = 2.dp)) {
                Text(
                    text = viewModel.getString(Constants.KEY_NAME)!!,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = viewModel.getString(Constants.KEY_EMAIL)!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults
            .mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ),
        navigationIcon = {
            IconButton(
                onClick = { /* doSomething() */ },
                modifier = Modifier
                    .size(48.dp)
            ) {
                Image(
                    painter = if (bitmap == null) painterResource(id = R.drawable.flower) else BitmapPainter(
                        bitmap?.asImageBitmap()!!
                    ),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            MaterialTheme.colorScheme.onPrimary,
                            shape = CircleShape
                        )
                        .clipToBounds(),
                    contentScale = ContentScale.Crop
                )
            }
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}