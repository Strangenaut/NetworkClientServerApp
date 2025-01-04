package com.strangenaut.networkclient.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrussianGreen,
    background = Shark,
    surface = OuterSpace,
    onSurface = Color.White,
    onSurfaceVariant = Emperor,
    outline = MineShaft,
    outlineVariant = Emperor,
    error = Thunderbird
)

private val LightColorScheme = lightColorScheme(
    primary = PrussianGreen,
    background = BlackSqueeze,
    surface = AquaHaze,
    onSurface = Color.Black,
    onSurfaceVariant = DisabledGray,
    outline = AthensGray,
    outlineVariant = TransparentGray,
    error = Thunderbird
)


@Composable
fun NetworkClientTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()

            val windowInsetsController = WindowCompat.getInsetsController(window, view)
            windowInsetsController.isAppearanceLightStatusBars = !darkTheme
            windowInsetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}