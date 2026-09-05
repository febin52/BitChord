package com.music.bitchord.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.music.bitchord.R

// Apple Music's signature red, used sparingly as the single accent.
val AccentRed = Color(0xFFFA2D48)

private val DarkColors = darkColorScheme(
    primary = AccentRed,
    onPrimary = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color(0xFF1C1C1F),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF222225),
    onSurfaceVariant = Color(0xFF8E8E93),
    outline = Color(0xFF38383A),
)

private val LightColors = lightColorScheme(
    primary = AccentRed,
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color(0xFFF7F7F9),
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF2F2F7),
    onSurfaceVariant = Color(0xFF6E6E73),
    outline = Color(0xFFE5E5EA),
)

/**
 * SF Pro Display for large titles, hero text, and prominent headings.
 */
val SFProDisplay = FontFamily(
    Font(R.font.sf_pro_display_regular, FontWeight.W400),
    Font(R.font.sf_pro_display_medium, FontWeight.W500),
    Font(R.font.sf_pro_display_semibold, FontWeight.W600),
    Font(R.font.sf_pro_display_bold, FontWeight.W700),
)

/**
 * SF Pro Text for body text, labels, buttons, metadata, and navigation.
 */
val SFProText = FontFamily(
    Font(R.font.sf_pro_display_regular, FontWeight.W400),
    Font(R.font.sf_pro_display_medium, FontWeight.W500),
    Font(R.font.sf_pro_display_semibold, FontWeight.W600),
    Font(R.font.sf_pro_display_bold, FontWeight.W700),
)

/**
 * Apple Music typography scale:
 * - Large page title: 34px, bold/semibold
 * - Section title: 22–24px, bold/semibold
 * - Album/playlist title & player title: 17px, semibold
 * - Primary body text: 17px, regular
 * - Secondary body text: 15px, regular
 * - Metadata / supporting text: 13–15px, regular
 * - Navigation/tab labels: 10–12px
 * - Buttons: 15–17px, medium/semibold
 * - Apple font weights: Regular (400), Medium (500), Semibold (600), Bold (700)
 */
private val AppleMusicTypography = Typography(
    // Large page title: 34px, bold (line height ~1.2, tracking tight)
    displayLarge = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.W700,
        fontSize = 34.sp,
        lineHeight = 41.sp,
        letterSpacing = (-0.4).sp,
    ),
    // Prominent hero headings: 28px, bold
    headlineLarge = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.W700,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.3).sp,
    ),
    // Section title: 22px, bold
    headlineMedium = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.W700,
        fontSize = 22.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.25).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.2).sp,
    ),
    // Prominent section header / modal title: 20px, semibold
    titleLarge = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.15).sp,
    ),
    // Album/playlist/song title & player title: 17px, semibold
    titleMedium = TextStyle(
        fontFamily = SFProText,
        fontWeight = FontWeight.W600,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.1).sp,
    ),
    // Filter chips / subheadings: 15px, semibold
    titleSmall = TextStyle(
        fontFamily = SFProText,
        fontWeight = FontWeight.W600,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.05).sp,
    ),
    // Primary body text: 17px, regular
    bodyLarge = TextStyle(
        fontFamily = SFProText,
        fontWeight = FontWeight.W400,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    // Secondary body text: 15px, regular
    bodyMedium = TextStyle(
        fontFamily = SFProText,
        fontWeight = FontWeight.W400,
        fontSize = 15.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.sp,
    ),
    // Metadata / supporting text: 13px, regular
    bodySmall = TextStyle(
        fontFamily = SFProText,
        fontWeight = FontWeight.W400,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),
    // Buttons: 16px, semibold
    labelLarge = TextStyle(
        fontFamily = SFProText,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 21.sp,
        letterSpacing = (-0.05).sp,
    ),
    // Player metadata / timestamps: 13px, regular
    labelMedium = TextStyle(
        fontFamily = SFProText,
        fontWeight = FontWeight.W400,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),
    // Navigation / tab labels: 11px, medium (500)
    labelSmall = TextStyle(
        fontFamily = SFProText,
        fontWeight = FontWeight.W500,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.sp,
    ),
)

@Composable
fun AppleMusicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppleMusicTypography,
        content = content,
    )
}

/**
 * Draws the status and navigation bar glyphs dark or light.
 *
 * `enableEdgeToEdge()` decides this from the *system* dark-mode setting, which
 * is the wrong input the moment the in-app theme disagrees with it: Light theme
 * on a phone in dark mode left white icons on a white bar, invisible. The bars
 * have to follow the theme the app is actually painting — with one exception,
 * the player, which is dark artwork regardless and so always wants light
 * glyphs. Hence a parameter rather than reading the theme here.
 */
@Composable
fun SystemBarIcons(dark: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return
    val window = (view.context as? Activity)?.window ?: return
    SideEffect {
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = dark
            isAppearanceLightNavigationBars = dark
        }
    }
}
