package com.example.tiksid.model

import androidx.annotation.DrawableRes
import com.example.tiksid.ui.common.Screen

data class NavigationItem(
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val screen: Screen
)
