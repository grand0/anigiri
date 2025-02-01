package tech.bnuuy.anigiri.feature.search.data.enumeration

import androidx.annotation.StringRes
import tech.bnuuy.anigiri.feature.search.R

enum class PublishStatus(
    val value: String,
    @StringRes val labelResId: Int,
) {
    IS_ONGOING(
        value = "IS_ONGOING",
        labelResId = R.string.ongoing_label,
    ),
    IS_NOT_ONGOING(
        value = "IS_NOT_ONGOING",
        labelResId = R.string.not_ongoing_label,
    ),
}
