package tech.bnuuy.anigiri.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val CardElevation = 2.dp
val CornerRadius = 8.dp

@Composable
fun ContentCard(
    modifier: Modifier = Modifier,
    elevation: Dp = CardElevation,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(CornerRadius))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(elevation))
            .then(modifier),
    ) {
        content()
    }
}
