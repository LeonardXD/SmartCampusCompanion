package com.example.smartcampuscompanion.ui.theme

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
    primary = PrimaryGreen80,
    onPrimary = Color(0xFF003918),
    primaryContainer = PrimaryGreen30,
    onPrimaryContainer = PrimaryGreen90,

    secondary = SecondarySage80,
    onSecondary = Color(0xFF233526),
    secondaryContainer = SecondarySage30,
    onSecondaryContainer = SecondarySage90,

    tertiary = TertiaryGold80,
    onTertiary = Color(0xFF3C2F05),
    tertiaryContainer = TertiaryGold30,
    onTertiaryContainer = TertiaryGold90,

    background = NeutralSurfaceDark,
    onBackground = NeutralOnSurfaceDark,
    surface = NeutralSurfaceDark,
    onSurface = NeutralOnSurfaceDark,

    surfaceVariant = Color(0xFF424942),
    onSurfaceVariant = Color(0xFFC2C8C2),
    outline = OutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen40,
    onPrimary = Color.White,
    primaryContainer = PrimaryGreen90,
    onPrimaryContainer = Color(0xFF00210A),

    secondary = SecondarySage40,
    onSecondary = Color.White,
    secondaryContainer = SecondarySage90,
    onSecondaryContainer = Color(0xFF0E1F12),

    tertiary = TertiaryGold40,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryGold90,
    onTertiaryContainer = Color(0xFF261A00),

    background = NeutralSurfaceLight,
    onBackground = NeutralOnSurfaceLight,
    surface = NeutralSurfaceLight,
    onSurface = NeutralOnSurfaceLight,

    surfaceVariant = Color(0xFFDEE4DE),
    onSurfaceVariant = Color(0xFF424942),
    outline = OutlineLight
)

@Composable
fun SmartCampusCompanionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is disabled by default to enforce the Premium Green Brand
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
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}