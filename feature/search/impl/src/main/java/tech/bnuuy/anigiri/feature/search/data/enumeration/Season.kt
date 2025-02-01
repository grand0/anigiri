package tech.bnuuy.anigiri.feature.search.data.enumeration

import androidx.annotation.StringRes
import tech.bnuuy.anigiri.feature.search.R

enum class Season(
    val value: String,
    @StringRes val labelResId: Int,
) {
    WINTER(
        value = "winter",
        labelResId = R.string.winter_label,
    ),
    SPRING(
        value = "spring",
        labelResId = R.string.spring_label,
    ),
    SUMMER(
        value = "summer",
        labelResId = R.string.summer_label,
    ),
    AUTUMN(
        value = "autumn",
        labelResId = R.string.autumn_label,
    ),
}
