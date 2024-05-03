package com.example.gameclock.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.gameclock.model.AppTheme


@Composable
fun GameClockTheme(
    appTheme: AppTheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme
    val typography: Typography
    when (appTheme) {

        AppTheme.Light -> {
            colorScheme = LightColorScheme
            typography = defaultTypography
        }

        AppTheme.Dark -> {
            colorScheme = DarkColorScheme
            typography = defaultTypography
        }

        AppTheme.Red -> {
            colorScheme = /*if (darkTheme) RedDarkColorScheme else*/ RedLightColorScheme
            typography = defaultTypography
        }

        AppTheme.CodeFall -> {
            colorScheme = /*if (darkTheme) CodeFallDarkColorScheme else */CodeFallLightColorScheme
            typography = defaultTypography
        }

        AppTheme.Space -> {
            colorScheme = DarkColorScheme
            typography = defaultTypography
        }

        AppTheme.DvdDark -> {
            colorScheme = DarkColorScheme
            typography = defaultTypography
        }

        AppTheme.DvdLight -> {
            colorScheme = LightColorScheme
            typography = defaultTypography
        }

        AppTheme.PinkAF -> {
            colorScheme = PinkAFColorScheme
            typography = defaultTypography
        }

        AppTheme.Cat -> {
            colorScheme = LightColorScheme
            typography = defaultTypography
        }

        else -> {
            colorScheme = DarkColorScheme
            typography = defaultTypography
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
        typography = typography,
        content = content
    )
}

private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)


private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

private val RedLightColorScheme = darkColorScheme(
    primary = md_theme_red_light_primary,
    onPrimary = md_theme_red_light_onPrimary,
    primaryContainer = md_theme_red_light_primaryContainer,
    onPrimaryContainer = md_theme_red_light_onPrimaryContainer,
    secondary = md_theme_red_light_secondary,
    onSecondary = md_theme_red_light_onSecondary,
    secondaryContainer = md_theme_red_light_secondaryContainer,
    onSecondaryContainer = md_theme_red_light_onSecondaryContainer,
    tertiary = md_theme_red_light_tertiary,
    onTertiary = md_theme_red_light_onTertiary,
    tertiaryContainer = md_theme_red_light_tertiaryContainer,
    onTertiaryContainer = md_theme_red_light_onTertiaryContainer,
    error = md_theme_red_light_error,
    errorContainer = md_theme_red_light_errorContainer,
    onError = md_theme_red_light_onError,
    onErrorContainer = md_theme_red_light_onErrorContainer,
    background = md_theme_red_light_background,
    onBackground = md_theme_red_light_onBackground,
    surface = md_theme_red_light_surface,
    onSurface = md_theme_red_light_onSurface,
    surfaceVariant = md_theme_red_light_surfaceVariant,
    onSurfaceVariant = md_theme_red_light_onSurfaceVariant,
    outline = md_theme_red_light_outline,
    inverseOnSurface = md_theme_red_light_inverseOnSurface,
    inverseSurface = md_theme_red_light_inverseSurface,
    inversePrimary = md_theme_red_light_inversePrimary,
    surfaceTint = md_theme_red_light_surfaceTint,
    outlineVariant = md_theme_red_light_outlineVariant,
    scrim = md_theme_red_light_scrim,
)

private val RedDarkColorScheme = darkColorScheme(
    primary = md_theme_red_dark_primary,
    onPrimary = md_theme_red_dark_onPrimary,
    primaryContainer = md_theme_red_dark_primaryContainer,
    onPrimaryContainer = md_theme_red_dark_onPrimaryContainer,
    secondary = md_theme_red_dark_secondary,
    onSecondary = md_theme_red_dark_onSecondary,
    secondaryContainer = md_theme_red_dark_secondaryContainer,
    onSecondaryContainer = md_theme_red_dark_onSecondaryContainer,
    tertiary = md_theme_red_dark_tertiary,
    onTertiary = md_theme_red_dark_onTertiary,
    tertiaryContainer = md_theme_red_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_red_dark_onTertiaryContainer,
    error = md_theme_red_dark_error,
    errorContainer = md_theme_red_dark_errorContainer,
    onError = md_theme_red_dark_onError,
    onErrorContainer = md_theme_red_dark_onErrorContainer,
    background = md_theme_red_dark_background,
    onBackground = md_theme_red_dark_onBackground,
    surface = md_theme_red_dark_surface,
    onSurface = md_theme_red_dark_onSurface,
    surfaceVariant = md_theme_red_dark_surfaceVariant,
    onSurfaceVariant = md_theme_red_dark_onSurfaceVariant,
    outline = md_theme_red_dark_outline,
    inverseOnSurface = md_theme_red_dark_inverseOnSurface,
    inverseSurface = md_theme_red_dark_inverseSurface,
    inversePrimary = md_theme_red_dark_inversePrimary,
    surfaceTint = md_theme_red_dark_surfaceTint,
    outlineVariant = md_theme_red_dark_outlineVariant,
    scrim = md_theme_red_dark_scrim,
)

private val CodeFallDarkColorScheme = darkColorScheme(
    primary = md_theme_codefall_dark_primary,
    onPrimary = md_theme_codefall_dark_onPrimary,
    primaryContainer = md_theme_codefall_dark_primaryContainer,
    onPrimaryContainer = md_theme_codefall_dark_onPrimaryContainer,
    secondary = md_theme_codefall_dark_secondary,
    onSecondary = md_theme_codefall_dark_onSecondary,
    secondaryContainer = md_theme_codefall_dark_secondaryContainer,
    onSecondaryContainer = md_theme_codefall_dark_onSecondaryContainer,
    tertiary = md_theme_codefall_dark_tertiary,
    onTertiary = md_theme_codefall_dark_onTertiary,
    tertiaryContainer = md_theme_codefall_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_codefall_dark_onTertiaryContainer,
    error = md_theme_codefall_dark_error,
    errorContainer = md_theme_codefall_dark_errorContainer,
    onError = md_theme_codefall_dark_onError,
    onErrorContainer = md_theme_codefall_dark_onErrorContainer,
    background = md_theme_codefall_dark_background,
    onBackground = md_theme_codefall_dark_onBackground,
    surface = md_theme_codefall_dark_surface,
    onSurface = md_theme_codefall_dark_onSurface,
    surfaceVariant = md_theme_codefall_dark_surfaceVariant,
    onSurfaceVariant = md_theme_codefall_dark_onSurfaceVariant,
    outline = md_theme_codefall_dark_outline,
    inverseOnSurface = md_theme_codefall_dark_inverseOnSurface,
    inverseSurface = md_theme_codefall_dark_inverseSurface,
    inversePrimary = md_theme_codefall_dark_inversePrimary,
    surfaceTint = md_theme_codefall_dark_surfaceTint,
    outlineVariant = md_theme_codefall_dark_outlineVariant,
    scrim = md_theme_codefall_dark_scrim,
    surfaceDim = md_theme_codefall_dark_shadow,
)

private val CodeFallLightColorScheme = darkColorScheme(
    primary = md_theme_codefall_light_primary,
    onPrimary = md_theme_codefall_light_onPrimary,
    primaryContainer = md_theme_codefall_light_primaryContainer,
    onPrimaryContainer = md_theme_codefall_light_onPrimaryContainer,
    secondary = md_theme_codefall_light_secondary,
    onSecondary = md_theme_codefall_light_onSecondary,
    secondaryContainer = md_theme_codefall_light_secondaryContainer,
    onSecondaryContainer = md_theme_codefall_light_onSecondaryContainer,
    tertiary = md_theme_codefall_light_tertiary,
    onTertiary = md_theme_codefall_light_onTertiary,
    tertiaryContainer = md_theme_codefall_light_tertiaryContainer,
    onTertiaryContainer = md_theme_codefall_light_onTertiaryContainer,
    error = md_theme_codefall_light_error,
    errorContainer = md_theme_codefall_light_errorContainer,
    onError = md_theme_codefall_light_onError,
    onErrorContainer = md_theme_codefall_light_onErrorContainer,
    background = md_theme_codefall_light_background,
    onBackground = md_theme_codefall_light_onBackground,
    surface = md_theme_codefall_light_surface,
    onSurface = md_theme_codefall_light_onSurface,
    surfaceVariant = md_theme_codefall_light_surfaceVariant,
    onSurfaceVariant = md_theme_codefall_light_onSurfaceVariant,
    outline = md_theme_codefall_light_outline,
    inverseOnSurface = md_theme_codefall_light_inverseOnSurface,
    inverseSurface = md_theme_codefall_light_inverseSurface,
    inversePrimary = md_theme_codefall_light_inversePrimary,
    surfaceTint = md_theme_codefall_light_surfaceTint,
    outlineVariant = md_theme_codefall_light_outlineVariant,
    scrim = md_theme_codefall_light_scrim,
    surfaceDim = md_theme_codefall_light_shadow,
)

private val PinkAFColorScheme = lightColorScheme(
    primary = md_theme_pink_af_primary,
    onPrimary = md_theme_pink_af_onPrimary,
    primaryContainer = md_theme_pink_af_primaryContainer,
    onPrimaryContainer = md_theme_pink_af_onPrimaryContainer,
    secondary = md_theme_pink_af_secondary,
    onSecondary = md_theme_pink_af_onSecondary,
    secondaryContainer = md_theme_pink_af_secondaryContainer,
    onSecondaryContainer = md_theme_pink_af_onSecondaryContainer,
    tertiary = md_theme_pink_af_tertiary,
    onTertiary = md_theme_pink_af_onTertiary,
    tertiaryContainer = md_theme_pink_af_tertiaryContainer,
    onTertiaryContainer = md_theme_pink_af_onTertiaryContainer,
    error = md_theme_pink_af_error,
    errorContainer = md_theme_pink_af_errorContainer,
    onError = md_theme_pink_af_onError,
    onErrorContainer = md_theme_pink_af_onErrorContainer,
    background = md_theme_pink_af_background,
    onBackground = md_theme_pink_af_onBackground,
    surface = md_theme_pink_af_surface,
    onSurface = md_theme_pink_af_onSurface,
    surfaceVariant = md_theme_pink_af_surfaceVariant,
    onSurfaceVariant = md_theme_pink_af_onSurfaceVariant,
    outline = md_theme_pink_af_outline,
    inverseOnSurface = md_theme_pink_af_inverseOnSurface,
    inverseSurface = md_theme_pink_af_inverseSurface,
    inversePrimary = md_theme_pink_af_inversePrimary,
//    surfaceTint = md_theme_pink_af_surfaceTint,
    outlineVariant = md_theme_pink_af_outlineVariant,
    scrim = md_theme_pink_af_scrim,
)
//private val red_lightColorScheme = lightColorScheme(
//    primary = md_theme_CHANGETHIS_primary,
//    onPrimary = md_theme_CHANGETHIS_onPrimary,
//    primaryContainer = md_theme_CHANGETHIS_primaryContainer,
//    onPrimaryContainer = md_theme_CHANGETHIS_onPrimaryContainer,
//    secondary = md_theme_CHANGETHIS_secondary,
//    onSecondary = md_theme_CHANGETHIS_onSecondary,
//    secondaryContainer = md_theme_CHANGETHIS_secondaryContainer,
//    onSecondaryContainer = md_theme_CHANGETHIS_onSecondaryContainer,
//    tertiary = md_theme_CHANGETHIS_tertiary,
//    onTertiary = md_theme_CHANGETHIS_onTertiary,
//    tertiaryContainer = md_theme_CHANGETHIS_tertiaryContainer,
//    onTertiaryContainer = md_theme_CHANGETHIS_onTertiaryContainer,
//    error = md_theme_CHANGETHIS_error,
//    errorContainer = md_theme_CHANGETHIS_errorContainer,
//    onError = md_theme_CHANGETHIS_onError,
//    onErrorContainer = md_theme_CHANGETHIS_onErrorContainer,
//    background = md_theme_CHANGETHIS_background,
//    onBackground = md_theme_CHANGETHIS_onBackground,
//    surface = md_theme_CHANGETHIS_surface,
//    onSurface = md_theme_CHANGETHIS_onSurface,
//    surfaceVariant = md_theme_CHANGETHIS_surfaceVariant,
//    onSurfaceVariant = md_theme_CHANGETHIS_onSurfaceVariant,
//    outline = md_theme_CHANGETHIS_outline,
//    inverseOnSurface = md_theme_CHANGETHIS_inverseOnSurface,
//    inverseSurface = md_theme_red_dark_inverseSurface,
//    inversePrimary = md_theme_CHANGETHIS_inversePrimary,
//    surfaceTint = md_theme_CHANGETHIS_surfaceTint,
//    outlineVariant = md_theme_CHANGETHIS_outlineVariant,
//    scrim = md_theme_CHANGETHIS_scrim,
//)