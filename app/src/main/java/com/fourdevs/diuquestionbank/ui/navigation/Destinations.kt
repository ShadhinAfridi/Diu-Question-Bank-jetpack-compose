package com.fourdevs.diuquestionbank.ui.navigation

import com.fourdevs.diuquestionbank.R

interface Destinations : IntentDestinations{
    val icon: Int
    val title: String
}

object Home: Destinations {
    override val route = "Home"
    override val icon = R.drawable.home
    override val title = "Home"
}

object Questions: Destinations {
    override val route = "Questions"
    override val icon = R.drawable.questions
    override val title = "Questions"
}

object Resources: Destinations {
    override val route = "Resources"
    override val icon = R.drawable.ic_resources_icon
    override val title = "Resources"
}

object Menu: Destinations {
    override val route = "Menu"
    override val icon = R.drawable.menu
    override val title = "Menu"
}

object Account: Destinations {
    override val route = "Account"
    override val icon = R.drawable.person
    override val title = "Account"
}


