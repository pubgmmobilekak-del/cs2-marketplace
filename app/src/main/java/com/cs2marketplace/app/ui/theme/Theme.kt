package com.cs2marketplace.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CS2DarkColorScheme = darkColorScheme(
    primary = OrangeAccent,
    onPrimary = TextPrimary,
    secondary = GoldAccent,
    background = BgDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    error = ErrorRed
)

@Composable
fun CS2MarketplaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Beta versiyada hozircha faqat qora (dark) mavzu ishlatiladi -
    // brend uslubi shunga mos keladi.
    MaterialTheme(
        colorScheme = CS2DarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
