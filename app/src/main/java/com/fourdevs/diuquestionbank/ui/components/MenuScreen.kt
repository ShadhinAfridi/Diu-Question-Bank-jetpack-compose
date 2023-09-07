package com.fourdevs.diuquestionbank.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R
import com.fourdevs.diuquestionbank.ui.navigation.AboutUs
import com.fourdevs.diuquestionbank.ui.navigation.AuthNav
import com.fourdevs.diuquestionbank.ui.navigation.ContactUs
import com.fourdevs.diuquestionbank.ui.navigation.Help
import com.fourdevs.diuquestionbank.ui.navigation.Menu
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel

@Composable
fun MenuScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(userViewModel)
        }
    ) {
        MenuItem(navController, authViewModel)
    }
}

@SuppressLint("QueryPermissionsNeeded")
@Composable
fun MenuItem(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {


    val link = "https://techerax.com/privacy-policy-for-diu-question-bank/"
    val joinUs = "https://forms.gle/thaSvMkraDACDd9c6"

    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    val joinUsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(joinUs))

    val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("market://details?id=${Constants.KEY_PACKAGE_NAME}")
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

@Composable
fun DrawerContent(userViewModel: UserViewModel) {
    val systemTheme = userViewModel.systemThemeFlow.collectAsState()
    ModalDrawerSheet {
        DropDownForDrawer(title = "Theme") {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 8.dp)
                    .wrapContentHeight()
            ) {
                Text(
                    text = "System",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Switch(
                    modifier = Modifier
                        .semantics { contentDescription = "System" },
                    checked = systemTheme.value,
                    onCheckedChange = {
                        userViewModel.updateTheme(it)
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = "Dark Mode",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Switch(
                    modifier = Modifier
                        .semantics { contentDescription = "Dark Mode" },
                    checked = !systemTheme.value,
                    onCheckedChange = {
                        userViewModel.updateTheme(!it)
                    }
                )
            }
        }
    }
}

@Composable
fun DropDownForDrawer(
    title: String,
    content:  @Composable (ColumnScope.() -> Unit)
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Divider()
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(),
            content = content
        )

    }
}
