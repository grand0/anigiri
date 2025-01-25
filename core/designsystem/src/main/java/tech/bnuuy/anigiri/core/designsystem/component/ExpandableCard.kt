package tech.bnuuy.anigiri.core.designsystem.component

import androidx.annotation.IntRange
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.bnuuy.anigiri.core.designsystem.theme.Typography

@Composable
fun ExpandableCard(
    text: String,
    title: String? = null,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
    @IntRange(from = 0) minimizedMaxLines: Int = 4,
    style: TextStyle = LocalTextStyle.current,
    fontStyle: FontStyle? = null,
    textAlign: TextAlign? = null,
    elevation: Dp = 2.dp
) {
    var expanded by remember { mutableStateOf(false) }
    var expandable by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(elevation))
            .clickable(expandable) { expanded = !expanded }
            .then(modifier),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (title != null) {
                    Text(
                        title,
                        style = Typography.titleMedium,
                        modifier = titleModifier.weight(1f)
                    )
                }
                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                )
            }
            Text(
                text,
                maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
                onTextLayout = {
                    if (!expanded && it.hasVisualOverflow) {
                        expandable = true
                    }
                },
                style = style,
                fontStyle = fontStyle,
                textAlign = textAlign,
                modifier = textModifier
                    .fillMaxWidth()
                    .animateContentSize()
            )
        }
    }
}
