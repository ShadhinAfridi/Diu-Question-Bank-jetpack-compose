package com.fourdevs.diuquestionbank.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.fourdevs.diuquestionbank.ui.navigation.Resources
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

    val enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
        -fullWidth / 3
    } + fadeIn(
        animationSpec = tween(durationMillis = 200)
    )
    val exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
        200
    } + fadeOut()

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = true,
                enter = enter,
                exit = exit
            ) {
                when (currentDestination?.route) {
                    Questions.route -> AppTopAppBar(name = "Questions")
                    Home.route -> HomeAppBar(authViewModel)
                    Account.route -> AccountTopAppBar(navController, authViewModel)
                    Menu.route -> MenuAppBar()
                    Resources.route -> AppTopAppBar(name = "Resources")
                }
            }

        },

        bottomBar = {
            AnimatedVisibility(
                visible = true,
                enter = enter,
                exit = exit
            ) {
                when (currentDestination?.route) {
                    Resources.route -> AnimatedNavigationBar(navController, 0)
                    Questions.route -> AnimatedNavigationBar(navController, 1)
                    Home.route -> AnimatedNavigationBar(navController, 2)
                    Account.route -> AnimatedNavigationBar(navController, 3)
                    Menu.route -> AnimatedNavigationBar(navController, 4)
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
    var selectedIndex by rememberSaveable { mutableIntStateOf(initialSelectedIndex) }

    val destinationList = listOf(
        Resources, Questions, Home, Account, Menu
    )

    val visible = selectedIndex != -1

    AnimatedVisibility(
        visible = visible,
        enter = expandIn(
            animationSpec = tween(100, easing = LinearOutSlowInEasing),
            expandFrom = Alignment.BottomStart
        ) {
            IntSize(50, 50)
        },
        exit = shrinkOut(
            tween(100, easing = FastOutSlowInEasing),
            shrinkTowards = Alignment.CenterStart
        ) { fullSize ->
            IntSize(fullSize.width / 10, fullSize.height / 5)
        }
    ) {
        NavigationBar(
            modifier = Modifier
                .height(56.dp)
                .alpha(if (visible) 1f else 0f),
            containerColor = MaterialTheme.colorScheme.background
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
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor =  MaterialTheme.colorScheme.onBackground,
                    )
                )
            }
        }
    }

}