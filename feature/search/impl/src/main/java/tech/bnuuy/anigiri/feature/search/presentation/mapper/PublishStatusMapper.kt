package tech.bnuuy.anigiri.feature.search.presentation.mapper

import androidx.annotation.StringRes
import tech.bnuuy.anigiri.core.network.datasource.enumeration.PublishStatus
import tech.bnuuy.anigiri.feature.search.R

@StringRes
fun PublishStatus.presentationNameId(): Int = when (this) {
    PublishStatus.IS_ONGOING -> R.string.ongoing_label
    PublishStatus.IS_NOT_ONGOING -> R.string.not_ongoing_label
}