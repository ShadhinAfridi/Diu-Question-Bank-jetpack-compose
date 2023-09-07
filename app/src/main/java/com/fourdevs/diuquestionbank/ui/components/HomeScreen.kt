package com.fourdevs.diuquestionbank.ui.components

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.ui.authentication.showToast
import com.fourdevs.diuquestionbank.ui.navigation.Department
import com.fourdevs.diuquestionbank.ui.navigation.EditProfileScreen
import com.fourdevs.diuquestionbank.ui.navigation.Help
import com.fourdevs.diuquestionbank.ui.navigation.Upload
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.NotificationViewModel
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    notificationViewModel: NotificationViewModel
) {
    var notificationAllowed by rememberSaveable {
        mutableStateOf(false)
    }
    var didOnce by rememberSaveable {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val activity = context as Activity
    if(!userViewModel.checkInternetConnection()) {
        showToast(context, "No internet!")
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
            Home(
                navController = navController,
                userViewModel = userViewModel
            )
        }
    }
}

@Composable
fun Home(
    navController: NavHostController,
    userViewModel: UserViewModel
) {

    var updateAvailable by rememberSaveable {
        mutableStateOf(false)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        userViewModel.checkForAppUpdates(LocalContext.current){ appUpdateInfo ->
            if(appUpdateInfo) {
                updateAvailable = true
            }
        }
    }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {

        if(updateAvailable) {
            val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=${Constants.KEY_PACKAGE_NAME}")
                setPackage("com.android.vending")
            }
            val startForResult = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { _ -> /* Handle result if needed */ }

            Snackbar(
                modifier = Modifier.padding(5.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                action = {
                    Button(
                        onClick = {
                            startForResult.launch(playStoreIntent)
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimary)
                    ) { Text(
                        text = "Update",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium
                    ) }
                }
            ) {
                Text(
                    text = "An update is available.",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
        HomeOptionCard(navController = navController, userViewModel = userViewModel)
    }
}

@Composable
fun HomeOptionCard(navController: NavHostController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    
    Spacer(modifier = Modifier.height(10.dp))

    HomeCard {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            item {
                IconCard(
                    title = "Questions",
                    icon = R.drawable.questions,
                    color = MaterialTheme.colorScheme.primary
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
                    title = "Account",
                    icon = R.drawable.person,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    navController.navigate(EditProfileScreen.route)
                }
            }
            item {
                IconCard(
                    title = "Help",
                    icon = R.drawable.ic_help,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    navController.navigate(Help.route)
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
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 5.dp)
            )
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
