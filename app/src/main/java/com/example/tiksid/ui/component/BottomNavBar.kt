package com.example.tiksid.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tiksid.R
import com.example.tiksid.model.NavigationItem
import com.example.tiksid.ui.common.Screen

@Composable
fun BottomNavBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent) // Hindari background di Box luar
    ) {
        // Layer Blur Background
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(16.dp) // Efek blur
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f)) // Transparansi
        )
        NavigationBar(
            modifier,
            containerColor = Color.Transparent,
        ) {
            val navBackStack by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStack?.destination?.route
            val navigationList = listOf(
                NavigationItem(
                    selectedIcon = R.drawable.home_filled,
                    unselectedIcon = R.drawable.home,
                    screen = Screen.Home
                ),
                NavigationItem(
                    selectedIcon = R.drawable.category_filled,
                    unselectedIcon = R.drawable.category,
                    screen = Screen.Search
                ),
                NavigationItem(
                    selectedIcon = R.drawable.ticket_filled,
                    unselectedIcon = R.drawable.ticket,
                    screen = Screen.Ticket
                ),
            )
            navigationList.map { menu ->
                NavigationBarItem(
                    selected = currentRoute == menu.screen.route,
                    onClick = {
                        navController.navigate(menu.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(
                                id = if (currentRoute == menu.screen.route) menu.selectedIcon
                                else if (currentRoute == Screen.Detail.route && menu.screen.route == Screen.Home.route) R.drawable.home_filled
                                else menu.unselectedIcon
                            ),
                            contentDescription = null,
                            modifier = modifier.size(24.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )
            }
        }
    }
}