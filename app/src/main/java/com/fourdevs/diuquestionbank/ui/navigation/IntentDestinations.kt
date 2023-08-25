package com.fourdevs.diuquestionbank.ui.navigation

interface IntentDestinations {
    val route: String
}

object Welcome: IntentDestinations {
    override val route: String
        get() = "Welcome"
}

object LogIn: IntentDestinations {
    override val route: String
        get() = "LogIn"
}

object ResetPassword: IntentDestinations {
    override val route: String
        get() = "ResetPassword"
}

object SignUp: IntentDestinations {
    override val route: String
        get() = "SignUp"
}

object Verification: IntentDestinations {
    override val route: String
        get() = "Verification"
}

object Upload: IntentDestinations {
    override val route: String
        get() = "Upload"
}

object AuthNav: IntentDestinations {
    override val route: String
        get() = "AuthNav"
}

object BottomNav: IntentDestinations {
    override val route: String
        get() = "BottomNav"
}

object Department: IntentDestinations {
    override val route: String
        get() = "Department"
}

object CourseList: IntentDestinations {
    override val route: String
        get() = "CourseList"
}

object QuestionList: IntentDestinations {
    override val route: String
        get() = "QuestionList"
}

object PdfViewer: IntentDestinations {
    override val route: String
        get() = "PdfViewer"
}

object EditProfileScreen: IntentDestinations {
    override val route: String
        get() = "EditProfileScreen"
}

object ChangePassword: IntentDestinations {
    override val route: String
        get() = "ChangePassword"
}

object AboutUs: IntentDestinations {
    override val route: String
        get() = "AboutUs"
}

object ContactUs: IntentDestinations {
    override val route: String
        get() = "ContactUs"
}

object Help: IntentDestinations {
    override val route: String
        get() = "Help"
}

object JoinUs: IntentDestinations {
    override val route: String
        get() = "JoinUs"
}