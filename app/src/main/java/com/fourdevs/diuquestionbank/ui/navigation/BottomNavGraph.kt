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
import com.fourdevs.diuquestionbank.ui.components.SolutionsScreen
import com.fourdevs.diuquestionbank.ui.components.UploadScreen
import com.fourdevs.diuquestionbank.viewmodel.AuthViewModel
import com.fourdevs.diuquestionbank.viewmodel.QuestionViewModel

@SuppressLint("ComposableNavGraphInComposeScope")
fun NavGraphBuilder.bottomNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    questionViewModel: QuestionViewModel
) {
    navigation(startDestination = Home.route, route = BottomNav.route) {
        composable(Home.route) {
            HomeScreen(navController)
        }
        composable(Questions.route) {
            QuestionsScreen(navController, questionViewModel)
        }
        composable(Solutions.route) {
            SolutionsScreen(navController)
        }
        composable(Account.route) {
            AccountScreen(navController)
        }
        composable(Menu.route) {
            MenuScreen(navController, authViewModel)
        }
        composable(Upload.route + "/{id}") { it ->
            val name = it.arguments?.getString("id")
            UploadScreen(navController, name)
        }
        composable(Department.route +"/{screenName}" + "/{department}") {
            val screenName = it.arguments?.getString("screenName")
            val departmentName = it.arguments?.getString("department")
            DepartmentScreen(screenName, departmentName, navController)
        }
        composable(CourseList.route + "/{screenName}" + "/{department}"+"/{shift}"+"/{exam}") {
            val screenName = it.arguments?.getString("screenName")
            val departmentName = it.arguments?.getString("department")
            val shift = it.arguments?.getString("shift")
            val exam = it.arguments?.getString("exam")
            CourseListScreen(screenName, departmentName, shift, exam, navController)
        }
        composable(QuestionList.route + "/{department}") {
            val department = it.arguments?.getString("department")

            QuestionList(department, navController, questionViewModel)
        }
        composable(PdfViewer.route + "/{fileName}/{id}/{code}/{isApproved}/{departmentName}") {
            val fileName = it.arguments?.getString("fileName")
            val id = it.arguments?.getString("id")
            val code = it.arguments?.getString("code")
            val isApproved = it.arguments?.getInt("isApproved")
            val departmentName = it.arguments?.getString("departmentName")
            PdfViewerScreen(fileName, id, code, isApproved, departmentName ,questionViewModel, navController)
        }

        composable(EditProfileScreen.route) {
            EditProfileScreen(navController)
        }

    }

}
