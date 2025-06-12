package tech.bnuuy.anigiri.feature.release.presentation.mapper

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.LibraryAdd
import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType
import tech.bnuuy.anigiri.feature.release.R

@StringRes
fun CollectionType?.presentationName(): Int = when (this) {
    CollectionType.PLANNED -> R.string.planned_label
    CollectionType.WATCHED -> R.string.watched_label
    CollectionType.WATCHING -> R.string.watching_label
    CollectionType.POSTPONED -> R.string.postponed_label
    CollectionType.ABANDONED -> R.string.abandoned_label
    null -> R.string.add_to_collection
}

fun CollectionType?.icon() = when (this) {
    CollectionType.PLANNED -> Icons.Default.CalendarMonth
    CollectionType.WATCHING -> Icons.Default.PlayArrow
    CollectionType.WATCHED -> Icons.Default.Visibility
    CollectionType.POSTPONED -> Icons.Default.Pause
    CollectionType.ABANDONED -> Icons.Default.Cancel
    null -> Icons.Outlined.LibraryAdd
}
