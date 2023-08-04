package com.fourdevs.diuquestionbank.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionsScreen(navController: NavHostController) {
    Scaffold(
        floatingActionButton = {
            IconButton(
                onClick = {
                    navController.navigate(
                        com.fourdevs.diuquestionbank.ui.navigation.Upload.route + "/solution"
                    )
                },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add Questions",
                    modifier = Modifier.padding(5.dp),
                    tint = Color.White
                )
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Department("Solution",navController)
        }
    }
}