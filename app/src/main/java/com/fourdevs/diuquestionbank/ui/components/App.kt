package com.fourdevs.diuquestionbank.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.fourdevs.diuquestionbank.ui.navigation.Solutions
import com.fourdevs.diuquestionbank.ui.navigation.authNavGraph
import com.fourdevs.diuquestionbank.ui.navigation.bottomNavGraph
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel


@Composable
fun App(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    questionViewModel: QuestionViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        topBar = {
            when (currentDestination?.route) {
                Questions.route ->
                    AppTopAppBar(name = "Questions")

                Solutions.route ->
                    AppTopAppBar(name = "Solutions")

                Home.route ->
                    HomeAppBar(authViewModel)

                Account.route ->
                    AccountTopAppBar(navController, authViewModel)

                Menu.route ->
                    MenuAppBar()
            }
        },

        bottomBar = {
            when (currentDestination?.route) {
                Questions.route ->
                    BottomNavBar(navController, 0)

                Solutions.route ->
                    BottomNavBar(navController, 1)

                Home.route ->
                    BottomNavBar(navController, 2)

                Account.route ->
                    BottomNavBar(navController, 3)

                Menu.route ->
                    BottomNavBar(navController, 4)
            }

        }
    ) {
        Box(Modifier.padding(it)) {
            NavHost(
                navController,
                startDestination = if (authViewModel.getBoolean(Constants.KEY_IS_VERIFIED)) BottomNav.route else AuthNav.route
            ) {
                authNavGraph(navController, authViewModel)
                bottomNavGraph(
                    navController,
                    authViewModel,
                    questionViewModel
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController, selected: Int) {
    ElevatedCard(
        modifier = Modifier.padding(top = 5.dp),
        content = { BottomNavigation(navController = navController, selected) }
    )
}

@SuppressLint("AutoboxingStateValueProperty")
@Composable
fun BottomNavigation(navController: NavController, selected: Int) {
    val destinationList = listOf(
        Questions, Solutions,
        Home, Account, Menu
    )
    var selectedIndex by rememberSaveable {
        mutableStateOf(selected)
    }

    NavigationBar(
        modifier = Modifier.height(56.dp),
        containerColor = MaterialTheme.colorScheme.onPrimary
    ) {
        destinationList.forEachIndexed { index, destinations ->
            NavigationBarItem(
                modifier = Modifier.padding(1.dp),
                icon = {
                    Icon(
                        painterResource(id = destinations.icon),
                        contentDescription = destinations.title,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(1.dp)
                    )
                },
                selected = index == selectedIndex,
                onClick = {
                    selectedIndex = index
                    navController.navigate(destinationList[index].route) {
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