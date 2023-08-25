package com.fourdevs.diuquestionbank.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.ui.navigation.AboutUs
import com.fourdevs.diuquestionbank.ui.navigation.AuthNav
import com.fourdevs.diuquestionbank.ui.navigation.ContactUs
import com.fourdevs.diuquestionbank.ui.navigation.Help
import com.fourdevs.diuquestionbank.ui.navigation.Menu
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel

@Composable
fun MenuScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    MenuItem(navController, authViewModel)
}

@SuppressLint("QueryPermissionsNeeded")
@Composable
fun MenuItem(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {

    val context = LocalContext.current
    val packageName = "com.fourdevs.diuquestionbank"
    val link = "https://techerax.com/"
    val joinUs = "https://forms.gle/thaSvMkraDACDd9c6"

    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    val joinUsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(joinUs))

    val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("market://details?id=$packageName")
        setPackage("com.android.vending") // Use the Play Store's package name
    }

    val startForResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ -> /* Handle result if needed */ }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        MenuTitleItem(title = "FourDevs")
        Column(modifier = Modifier.fillMaxWidth()) {
            MenuRowItem(imageId = R.drawable.info, title = "About Us") {
                navController.navigate(AboutUs.route)
            }
            MenuRowItem(imageId = R.drawable.call, title = "Contact Us") {
                navController.navigate(ContactUs.route)
            }
            MenuRowItem(imageId = R.drawable.help, title = "Help") {
                navController.navigate(Help.route)
            }
            MenuRowItem(imageId = R.drawable.group, title = "Join Us") {
                startForResult.launch(joinUsIntent)
            }
            MenuRowItem(imageId = R.drawable.star, title = "Rate This App") {
                startForResult.launch(playStoreIntent)
            }
            MenuRowItem(imageId = R.drawable.shield, title = "Privacy Policy") {
                startForResult.launch(browserIntent)
            }
            MenuRowItem(imageId = R.drawable.logout, title = "Log Out") {
                authViewModel.logout()
                navController.navigate(AuthNav.route) {
                    popUpTo(Menu.route) { inclusive = true }
                }
            }
        }
    }
}


@Composable
fun MenuTitleItem(title: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 5.dp)
                .background(color = MaterialTheme.colorScheme.outline)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuRowItem(imageId: Int, title: String, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .padding(5.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {

        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start

        ) {
            Icon(
                painter = painterResource(id = imageId),
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 5.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1
            )
        }
        
    }
}