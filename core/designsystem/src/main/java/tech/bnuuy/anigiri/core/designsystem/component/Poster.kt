package tech.bnuuy.anigiri.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

const val PosterAspectRatio = 2f / 3f

@Composable
fun Poster(
    painter: Painter? = null,
    isLoading: Boolean = false,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .padding(16.dp)
            .width(100.dp)
            .aspectRatio(PosterAspectRatio)
    ) {
        if (isLoading) {
            ShimmerLoader(
                Modifier.fillMaxSize()
            )
        } else if (isError) {
            Icon(
                Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center),
            )
        } else if (painter != null) {
            Image(
                painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .fillMaxSize(),
            )
        }
    }
}
