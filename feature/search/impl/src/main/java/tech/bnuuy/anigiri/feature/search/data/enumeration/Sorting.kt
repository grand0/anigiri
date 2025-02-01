package tech.bnuuy.anigiri.feature.search.data.enumeration

import androidx.annotation.StringRes
import tech.bnuuy.anigiri.feature.search.R

enum class Sorting(
    val value: String,
    @StringRes val labelResId: Int,
) {
    UPDATED_DESC(
        value = "FRESH_AT_DESC",
        labelResId = R.string.updated_desc_label,
    ),
    UPDATED_ASC(
        value = "FRESH_AT_ASC",
        labelResId = R.string.updated_asc_label,
    ),
    RATING_DESC(
        value = "RATING_DESC",
        labelResId = R.string.rating_desc_label,
    ),
    RATING_ASC(
        value = "RATING_ASC",
        labelResId = R.string.rating_asc_label,
    ),
    YEAR_DESC(
        value = "YEAR_DESC",
        labelResId = R.string.year_desc_label,
    ),
    YEAR_ASC(
        value = "YEAR_ASC",
        labelResId = R.string.year_asc_label,
    ),
}
