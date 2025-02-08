package tech.bnuuy.anigiri.core.designsystem.component

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ExpandableTextCard(
    text: String,
    title: String? = null,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
    @IntRange(from = 0) minimizedMaxLines: Int = 4,
    style: TextStyle = LocalTextStyle.current,
    fontStyle: FontStyle? = null,
    textAlign: TextAlign? = null,
) {
    var expandable by remember { mutableStateOf(false) }
    
    ExpandableCard(
        title = title,
        modifier = modifier,
        titleModifier = titleModifier,
        expandable = expandable,
    ) { expanded ->
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
        )
    }
}
