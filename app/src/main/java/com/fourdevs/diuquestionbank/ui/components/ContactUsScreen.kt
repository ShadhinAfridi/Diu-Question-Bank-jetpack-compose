package com.fourdevs.diuquestionbank.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ContactUsScreen(navController: NavController) {
    ContactUsPage(navController = navController)
}

data class ContactInfo(
    val icon: ImageVector,
    val label: String,
    val value: String
)


val contactInfoList = listOf(
    ContactInfo(
        icon = Icons.Default.Email,
        label = "DIU Question Bank",
        value = "diuquestionbank@gmail.com"
    ),
    ContactInfo(
        icon = Icons.Default.Email,
        label = "FourDevs",
        value = "fourdevs.pro@gmail.com"
    )
)

@Composable
fun ContactUsPage(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(navController = navController, name = "Contact Us")
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column {
                for(contact in contactInfoList) {
                    ContactInfoItem(contactInfo = contact)
                }
            }
        }
    }
}

@Composable
fun ContactInfoItem(contactInfo: ContactInfo) {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val emailIntent = Intent(
                    Intent.ACTION_SENDTO,
                    Uri.fromParts("mailto", contactInfo.value, null)
                )
                context.startActivity(emailIntent)
            }
    ) {
        Icon(
            imageVector = contactInfo.icon,
            contentDescription = contactInfo.label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = contactInfo.label, style = MaterialTheme.typography.titleSmall)
            Text(text = contactInfo.value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

