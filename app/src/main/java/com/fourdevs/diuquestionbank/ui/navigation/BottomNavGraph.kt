package com.fourdevs.diuquestionbank.ui.navigation

import android.annotation.SuppressLint
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.fourdevs.diuquestionbank.ui.components.AccountScreen
import com.fourdevs.diuquestionbank.ui.components.CourseListScreen
import com.fourdevs.diuquestionbank.ui.components.DepartmentScreen
import com.fourdevs.diuquestionbank.ui.components.EditProfileScreen
import com.fourdevs.diuquestionbank.ui.components.HomeScreen
import com.fourdevs.diuquestionbank.ui.components.MenuScreen
import com.fourdevs.diuquestionbank.ui.components.PdfViewerScreen
import com.fourdevs.diuquestionbank.ui.components.QuestionList
import com.fourdevs.diuquestionbank.ui.components.QuestionsScreen
import com.fourdevs.diuquestionbank.ui.components.UploadScreen
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel

@SuppressLint("ComposableNavGraphInComposeScope")
fun NavGraphBuilder.bottomNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    questionViewModel: QuestionViewModel,
    userViewModel: UserViewModel
) {
    navigation(startDestination = Home.route, route = BottomNav.route) {
        composable(Home.route) {
            HomeScreen(navController)
        }
        composable(Questions.route) {
            QuestionsScreen(navController, questionViewModel)
        }
        composable(Account.route) {
            AccountScreen(navController)
        }
        composable(Menu.route) {
            MenuScreen(navController, authViewModel)
        }
        composable(Upload.route + "/{id}") {
            val name = it.arguments?.getString("id")
            UploadScreen(navController, name)
        }
        composable(Department.route + "/{department}") {
            val departmentName = it.arguments?.getString("department")
            DepartmentScreen(departmentName, navController)
        }
        composable(CourseList.route + "/{department}"+"/{shift}"+"/{exam}") {
            val departmentName = it.arguments?.getString("department")
            val shift = it.arguments?.getString("shift")
            val exam = it.arguments?.getString("exam")
            CourseListScreen(departmentName, shift, exam, navController, questionViewModel)
        }
        composable(QuestionList.route + "/{department}"+ "/{courseName}" + "/{shift}" + "/{exam}") {
            val department = it.arguments?.getString("department")
            val courseName = it.arguments?.getString("courseName")
            val shift = it.arguments?.getString("shift")
            val exam = it.arguments?.getString("exam")

            QuestionList(department, courseName, shift, exam, navController, questionViewModel)
        }
        composable(PdfViewer.route + "/{fileName}/{id}") {
            val fileName = it.arguments?.getString("fileName")
            val id = it.arguments?.getString("id")
            PdfViewerScreen(fileName, id, navController)
        }

        composable(EditProfileScreen.route) {
            EditProfileScreen(navController, userViewModel)
        }

    }

}
