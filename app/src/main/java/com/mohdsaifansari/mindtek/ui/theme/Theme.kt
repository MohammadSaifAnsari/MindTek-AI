package com.mohdsaifansari.mindtek.ui.theme

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = dark_Primary_Color,
    secondary = dark_Secondary_Color,
    tertiary = dark_tertiary_Color,
    background = dark_Background_Color,
    surface = dark_Surface_Color,
    onBackground = dark_OnBackground_Color, onSurface = dark_OnSurface_Color,
    onSurfaceVariant = dark_OnSurfaceVarient_Color, surfaceContainer = dark_SurfaceContainer_Color
)

private val LightColorScheme = lightColorScheme(
    primary = light_Primary_Color,
    secondary = light_Secondary_Color,
    tertiary = light_tertiary_Color,
    background = light_Background_Color,
    surface = light_Surface_Color,
    onBackground = light_OnBackground_Color,
    onSurface = light_OnSurface_Color,
    onSurfaceVariant = light_OnSurfaceVarient_Color,
    surfaceContainer = light_SurfaceContainer_Color

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
fun MindtekTheme(
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
            val window = (view.context as Activity).window
            window.statusBarColor = Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}