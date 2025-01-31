package tech.bnuuy.anigiri.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import tech.bnuuy.anigiri.core.designsystem.theme.Typography

@Composable
fun PosterListItem(
    title: String? = null,
    description: String? = null,
    descriptionMaxLines: Int = 4,
    painter: Painter? = null,
    isImageLoading: Boolean = false,
    isImageError: Boolean = false,
    titleStyle: TextStyle = Typography.titleLarge,
    descriptionStyle: TextStyle = Typography.bodySmall,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
) {
    Row(modifier) {
        if (painter != null || isImageLoading || isImageError) {
            Poster(
                painter = painter,
                isLoading = isImageLoading,
                isError = isImageError,
                modifier = imageModifier,
            )
        }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(8.dp)
        ) {
            if (title != null) {
                Text(
                    title,
                    style = titleStyle,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }
            if (description != null) {
                Text(
                    description,
                    maxLines = descriptionMaxLines,
                    overflow = TextOverflow.Ellipsis,
                    style = descriptionStyle,
                )
            }
        }
    }
}
