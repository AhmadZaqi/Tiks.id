package com.example.tiksid.ui.common

sealed class Screen(val route: String) {
    data object Home: Screen("Home")
    data object Login: Screen("Login")
    data object Search: Screen("Search")
    data object Ticket: Screen("Ticket")
    data object Detail: Screen("Detail/{MovieId}"){
        fun createRoute(movieId: Int) = "Detail/$movieId"
    }
}