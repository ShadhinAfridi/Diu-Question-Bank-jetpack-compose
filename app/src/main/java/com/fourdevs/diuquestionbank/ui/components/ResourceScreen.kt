package com.fourdevs.diuquestionbank.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel

@Composable
fun ResourceScreen(
    navController: NavHostController,
    userViewModel: UserViewModel
) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ResourcesItem() {

    ElevatedCard(
        onClick = {},
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {

    }

}