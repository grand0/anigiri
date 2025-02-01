package tech.bnuuy.anigiri.feature.search.data.enumeration

import androidx.annotation.StringRes
import tech.bnuuy.anigiri.feature.search.R

enum class AgeRating(
    val value: String,
    @StringRes val labelResId: Int,
) {
    R0(
        value = "R0_PLUS",
        labelResId = R.string.r0_label,
    ),
    R6(
        value = "R6_PLUS",
        labelResId = R.string.r6_label,
    ),
    R12(
        value = "R12_PLUS",
        labelResId = R.string.r12_label,
    ),
    R16(
        value = "R16_PLUS",
        labelResId = R.string.r16_label,
    ),
    R18(
        value = "R18_PLUS",
        labelResId = R.string.r18_label,
    ),
}
