package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppThemeMode {
  DARK,
  LIGHT,
  SYSTEM
}

val DarkColorScheme =
  darkColorScheme(
    primary = VioletPrimary,
    onPrimary = Color.White,
    primaryContainer = VioletDeep,
    onPrimaryContainer = VioletTertiary,
    secondary = VioletSecondary,
    onSecondary = ObsidianBlack,
    secondaryContainer = ObsidianCardBg,
    onSecondaryContainer = VioletSecondary,
    tertiary = NeonCyan,
    onTertiary = ObsidianBlack,
    background = ObsidianBlack,
    onBackground = TextPrimaryDark,
    surface = ObsidianDarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = ObsidianCardBg,
    onSurfaceVariant = TextSecondaryDark,
    outline = ObsidianCardBorder,
  )

val LightColorScheme =
  lightColorScheme(
    primary = VioletPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = VioletTertiary,
    onPrimaryContainer = VioletDeep,
    secondary = VioletPrimary,
    onSecondary = Color.White,
    secondaryContainer = LightCardBg,
    onSecondaryContainer = VioletPrimaryLight,
    tertiary = NeonCyan,
    onTertiary = Color.White,
    background = LightBg,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightCardBg,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightCardBorder,
  )

@Composable
fun KnowConnectTheme(
  themeMode: AppThemeMode = AppThemeMode.DARK,
  content: @Composable () -> Unit,
) {
  val darkTheme =
    when (themeMode) {
      AppThemeMode.DARK -> true
      AppThemeMode.LIGHT -> false
      AppThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content,
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  KnowConnectTheme(
    themeMode = if (darkTheme) AppThemeMode.DARK else AppThemeMode.LIGHT,
    content = content,
  )
}
