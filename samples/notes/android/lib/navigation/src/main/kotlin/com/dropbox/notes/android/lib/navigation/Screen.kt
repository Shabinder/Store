package com.dropbox.notes.android.lib.navigation

import androidx.annotation.DrawableRes


private const val HOME_ROUTE = "/home"
private const val ACCOUNT_ROUTE = "/profile"
private const val SEARCH_ROUTE = "/search"
private const val SETTINGS_ROUTE = "/settings"
private const val EXPLORE_ROUTE = "/explore"

private const val HOME = "Home"
private const val ACCOUNT = "Account"
private const val SEARCH = "Search"
private const val SETTINGS = "Settings"
private const val EXPLORE = "Explore"


sealed class Screen(
    val route: String,
    val title: String,
    @DrawableRes val iconSelected: Int,
    @DrawableRes val iconNotSelected: Int
) {
    object Home : Screen(HOME_ROUTE, HOME, R.drawable.ic_dig_home_fill, R.drawable.ic_dig_home_line)
    object Account : Screen(ACCOUNT_ROUTE, ACCOUNT, R.drawable.ic_person_fill, R.drawable.ic_dig_person_line)
    object Search : Screen(SEARCH_ROUTE, SEARCH, R.drawable.ic_dig_search_line, R.drawable.ic_dig_search_line)
    object Settings : Screen(SETTINGS_ROUTE, SETTINGS, R.drawable.ic_dig_settings_fill, R.drawable.ic_dig_settings_line)
    object Explore : Screen(EXPLORE_ROUTE, EXPLORE, R.drawable.ic_globe_fill, R.drawable.ic_globe_fill)
}