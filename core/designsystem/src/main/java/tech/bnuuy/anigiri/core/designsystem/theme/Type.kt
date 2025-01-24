package tech.bnuuy.anigiri.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import tech.bnuuy.anigiri.core.designsystem.R

@OptIn(ExperimentalTextApi::class)
val defaultFontFamily = FontFamily(
    Font(R.font.raleway_light, FontWeight.Light),
    Font(R.font.raleway_regular, FontWeight.Normal),
    Font(R.font.raleway_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.raleway_medium, FontWeight.Medium),
    Font(R.font.raleway_semibold, FontWeight.SemiBold),
    Font(R.font.raleway_bold, FontWeight.Bold),
)

val Typography = Typography(
    displayLarge = Typography().displayLarge.copy(fontFamily = defaultFontFamily),
    displayMedium = Typography().displayMedium.copy(fontFamily = defaultFontFamily),
    displaySmall = Typography().displaySmall.copy(fontFamily = defaultFontFamily),

    headlineLarge = Typography().headlineLarge.copy(fontFamily = defaultFontFamily),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = defaultFontFamily),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = defaultFontFamily),

    titleLarge = Typography().titleLarge.copy(fontFamily = defaultFontFamily),
    titleMedium = Typography().titleMedium.copy(fontFamily = defaultFontFamily),
    titleSmall = Typography().titleSmall.copy(fontFamily = defaultFontFamily),

    bodyLarge = Typography().bodyLarge.copy(fontFamily = defaultFontFamily),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = defaultFontFamily),
    bodySmall = Typography().bodySmall.copy(fontFamily = defaultFontFamily),

    labelLarge = Typography().labelLarge.copy(fontFamily = defaultFontFamily),
    labelMedium = Typography().labelMedium.copy(fontFamily = defaultFontFamily),
    labelSmall = Typography().labelSmall.copy(fontFamily = defaultFontFamily),
)
