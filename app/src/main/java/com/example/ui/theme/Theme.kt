package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val CommerceLightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = OnNavyPrimary,
    primaryContainer = NavyContainer,
    onPrimaryContainer = OnNavyContainer,
    secondary = EmeraldSecondary,
    onSecondary = OnEmeraldSecondary,
    secondaryContainer = EmeraldContainer,
    onSecondaryContainer = OnEmeraldContainer,
    tertiary = GoldTertiary,
    onTertiary = OnGoldTertiary,
    tertiaryContainer = GoldContainer,
    onTertiaryContainer = OnGoldContainer,
    background = IvoryBackground,
    onBackground = InkOnSurface,
    surface = PearlSurface,
    onSurface = InkOnSurface,
    surfaceVariant = SlateSurfaceVariant,
    onSurfaceVariant = MutedInkOnSurfaceVariant,
    outline = SubtleOutline,
    error = ErrorCrimson
)

private val CommerceDarkColorScheme = darkColorScheme(
    primary = DarkNavyPrimary,
    onPrimary = DarkOnNavyPrimary,
    primaryContainer = DarkNavyContainer,
    onPrimaryContainer = DarkOnNavyContainer,
    secondary = DarkEmeraldSecondary,
    onSecondary = DarkOnEmeraldSecondary,
    secondaryContainer = DarkEmeraldContainer,
    onSecondaryContainer = DarkOnEmeraldContainer,
    tertiary = DarkGoldTertiary,
    onTertiary = DarkOnGoldTertiary,
    tertiaryContainer = DarkGoldContainer,
    onTertiaryContainer = DarkOnGoldContainer,
    background = MidnightBackground,
    onBackground = CrispLightInk,
    surface = MidnightSurface,
    onSurface = CrispLightInk,
    surfaceVariant = MidnightSurfaceVariant,
    onSurfaceVariant = MutedLightInk,
    outline = DarkSubtleOutline
)

private val CommerceShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun CommerceHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CommerceDarkColorScheme else CommerceLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = CommerceTypography,
        shapes = CommerceShapes,
        content = content
    )
}
