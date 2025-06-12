package tech.bnuuy.anigiri.feature.collections.presentation.mapper

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Visibility
import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType
import tech.bnuuy.anigiri.feature.collections.R

@StringRes
fun CollectionType.presentationName(): Int = when (this) {
    CollectionType.PLANNED -> R.string.planned_label
    CollectionType.WATCHED -> R.string.watched_label
    CollectionType.WATCHING -> R.string.watching_label
    CollectionType.POSTPONED -> R.string.postponed_label
    CollectionType.ABANDONED -> R.string.abandoned_label
}

fun CollectionType.icon(selected: Boolean) = if (selected) when (this) {
    CollectionType.PLANNED -> Icons.Filled.CalendarMonth
    CollectionType.WATCHING -> Icons.Filled.PlayArrow
    CollectionType.WATCHED -> Icons.Filled.Visibility
    CollectionType.POSTPONED -> Icons.Filled.Pause
    CollectionType.ABANDONED -> Icons.Filled.Cancel
} else when (this) {
    CollectionType.PLANNED -> Icons.Outlined.CalendarMonth
    CollectionType.WATCHING -> Icons.Outlined.PlayArrow
    CollectionType.WATCHED -> Icons.Outlined.Visibility
    CollectionType.POSTPONED -> Icons.Outlined.Pause
    CollectionType.ABANDONED -> Icons.Outlined.Cancel
}
