package tech.bnuuy.anigiri.core.designsystem

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import kotlin.time.Duration

@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val dir = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(dir) + other.calculateStartPadding(dir),
        top = calculateTopPadding() + other.calculateTopPadding(),
        end = calculateEndPadding(dir) + other.calculateEndPadding(dir),
        bottom = calculateBottomPadding() + calculateBottomPadding(),
    )
}

fun Duration.format() = toComponents { hours, minutes, seconds, _ ->
    val minutesStr = "$minutes".padStart(2, '0')
    val secondsStr = "$seconds".padStart(2, '0')
    if (hours == 0L) {
        "${minutesStr}:${secondsStr}"
    } else {
        "${hours}:${minutesStr}:${secondsStr}"
    }
}
