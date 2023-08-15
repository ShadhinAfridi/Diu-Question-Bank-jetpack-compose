package com.fourdevs.diuquestionbank.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fourdevs.diuquestionbank.ui.navigation.Account
import com.fourdevs.diuquestionbank.ui.navigation.AuthNav
import com.fourdevs.diuquestionbank.ui.navigation.BottomNav
import com.fourdevs.diuquestionbank.ui.navigation.Home
import com.fourdevs.diuquestionbank.ui.navigation.Menu
import com.fourdevs.diuquestionbank.ui.navigation.Questions
import com.fourdevs.diuquestionbank.ui.navigation.authNavGraph
import com.fourdevs.diuquestionbank.ui.navigation.bottomNavGraph
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel


@Composable
fun App(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    questionViewModel: QuestionViewModel,
    userViewModel: UserViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                when (currentDestination?.route) {
                    Questions.route -> AppTopAppBar(name = "Questions")
                    Home.route -> HomeAppBar(authViewModel)
                    Account.route -> AccountTopAppBar(navController, authViewModel)
                    Menu.route -> MenuAppBar()
                }
            }

        },

        bottomBar = {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                when (currentDestination?.route) {
                    Questions.route -> AnimatedNavigationBar(navController, 0)
                    Home.route -> AnimatedNavigationBar(navController, 1)
                    Account.route -> AnimatedNavigationBar(navController, 2)
                    Menu.route -> AnimatedNavigationBar(navController, 3)
                }
            }

        }) {
        Box(Modifier.padding(it)) {
            NavHost(
                navController,
                startDestination = if (authViewModel.getBoolean(Constants.KEY_IS_VERIFIED)) BottomNav.route else AuthNav.route
            ) {
                authNavGraph(navController, authViewModel)
                bottomNavGraph(
                    navController, authViewModel, questionViewModel, userViewModel
                )
            }
        }
    }
}

@Composable
fun AnimatedNavigationBar(
    navController: NavController,
    initialSelectedIndex: Int
) {
    var selectedIndex by rememberSaveable { mutableStateOf(initialSelectedIndex) }

    val destinationList = listOf(
        Questions, Home, Account, Menu
    )

    val visible = selectedIndex != -1

    AnimatedVisibility(
        visible = visible,
        // Set the start width to 20 (pixels), 0 by default
        enter = expandIn(
            // Overwrites the default spring animation with tween
            animationSpec = tween(100, easing = LinearOutSlowInEasing),
            // Overwrites the corner of the content that is first revealed
            expandFrom = Alignment.BottomStart
        ) {
            // Overwrites the initial size to 50 pixels by 50 pixels
            IntSize(50, 50)
        },
        exit = shrinkOut(
            tween(100, easing = FastOutSlowInEasing),
            // Overwrites the area of the content that the shrink animation will end on. The
            // following parameters will shrink the content's clip bounds from the full size of the
            // content to 1/10 of the width and 1/5 of the height. The shrinking clip bounds will
            // always be aligned to the CenterStart of the full-content bounds.
            shrinkTowards = Alignment.CenterStart
        ) { fullSize ->
            // Overwrites the target size of the shrinking animation.
            IntSize(fullSize.width / 10, fullSize.height / 5)
        }
    ) {
        NavigationBar(
            modifier = Modifier
                .height(56.dp)
                .alpha(if (visible) 1f else 0f),
            containerColor = MaterialTheme.colorScheme.onPrimary
        ) {
            destinationList.forEachIndexed { index, destination ->
                val isSelected = index == selectedIndex

                NavigationBarItem(
                    modifier = Modifier.padding(1.dp),
                    icon = {
                        Icon(
                            painterResource(id = destination.icon),
                            contentDescription = destination.title,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(1.dp)
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        selectedIndex = index
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }

}