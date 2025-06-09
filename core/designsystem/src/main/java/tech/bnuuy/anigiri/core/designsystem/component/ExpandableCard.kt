package tech.bnuuy.anigiri.core.designsystem.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tech.bnuuy.anigiri.core.designsystem.theme.Typography

@Composable
fun ExpandableCard(
    title: String? = null,
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
    expandable: Boolean = true,
    onExpansionChanged: (expanded: Boolean) -> Unit = { },
    content: @Composable ColumnScope.(expanded: Boolean) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ContentCard (
        modifier = Modifier
            .clickable(expandable) {
                expanded = !expanded
                onExpansionChanged(expanded)
            }
            .animateContentSize()
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
                if (expandable) {
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                    )
                }
            }
            
            content(expanded)
        }
    }
}
