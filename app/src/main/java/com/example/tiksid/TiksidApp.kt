package com.example.tiksid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tiksid.data.local.TokenController
import com.example.tiksid.ui.common.Screen
import com.example.tiksid.ui.component.BottomNavBar
import com.example.tiksid.ui.screen.DetailMovieScreen
import com.example.tiksid.ui.screen.HomeScreen
import com.example.tiksid.ui.screen.LoginScreen
import com.example.tiksid.ui.screen.SearchScreen
import com.example.tiksid.ui.screen.TicketScreen

@Composable
fun TiksIdApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route
    val context = LocalContext.current

    Scaffold{ innerPadding->
        Box(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            NavHost(
                navController = navController,
                startDestination = if (TokenController(context).isExpired()) Screen.Login.route else Screen.Home.route,
//                modifier = Modifier.padding(bottom = 64.dp),
            ) {
                composable(Screen.Login.route) {
                    LoginScreen(
                        navToMainScreen = {
                            navController.navigate(Screen.Home.route)
                        }
                    )
                }
                composable(Screen.Home.route) {
                    HomeScreen(
                        navToDetailScreen = {
                            navController.navigate(Screen.Detail.createRoute(it))
                        }
                    )
                }
                composable(Screen.Search.route) {
                    SearchScreen(
                        navToDetailScreen = {
                            navController.navigate(Screen.Detail.createRoute(it))
                        }
                    )
                }
                composable(Screen.Ticket.route) { TicketScreen() }
                composable(
                    Screen.Detail.route,
                    arguments = listOf(
                        navArgument("MovieId"){
                            type = NavType.IntType
                        }
                    )
                ) {
                    val id = it.arguments?.getInt("MovieId")?: -1
                    DetailMovieScreen(
                        movieId = id
                    )
                }
            }

            if (currentRoute != Screen.Login.route) BottomNavBar(navController = navController, modifier = Modifier.align(
                Alignment.BottomCenter
            ))
        }
    }
}