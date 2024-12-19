package com.example.tiksid.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CrownGold,
    onPrimary = Color.White,
    secondary = GlamorousGold,
    secondaryContainer = ElegantBlack,
    onSecondaryContainer = CrownGold,
    tertiary = SoftGold,
    background = ElegantBlack,
    onBackground = Color.White,
    primaryContainer = MysteriousSlate,
    onPrimaryContainer = Color.White,
    onSurfaceVariant = SoftGold
)

private val LightColorScheme = lightColorScheme(
    primary = CrownGold,
    onPrimary = Color.White,
    secondary = GlamorousGold,
    secondaryContainer = ElegantBlack,
    onSecondaryContainer = CrownGold,
    tertiary = SoftGold,
    background = ElegantBlack,
    onBackground = Color.White,
    primaryContainer = MysteriousSlate,
    onPrimaryContainer = Color.White,
    onSurfaceVariant = SoftGold

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TiksidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode){
        SideEffect {
            val activity = view.context as Activity
            activity.window.statusBarColor = colorScheme.background.toArgb()
            activity.window.isStatusBarContrastEnforced = true
//            WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}