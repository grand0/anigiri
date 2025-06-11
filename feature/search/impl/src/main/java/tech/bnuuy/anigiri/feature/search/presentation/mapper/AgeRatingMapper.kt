package tech.bnuuy.anigiri.feature.search.presentation.mapper

import androidx.annotation.StringRes
import tech.bnuuy.anigiri.core.network.datasource.enumeration.AgeRating
import tech.bnuuy.anigiri.feature.search.R

@StringRes
fun AgeRating.presentationNameId(): Int = when (this) {
    AgeRating.R0 -> R.string.r0_label
    AgeRating.R6 -> R.string.r6_label
    AgeRating.R12 -> R.string.r12_label
    AgeRating.R16 -> R.string.r16_label
    AgeRating.R18 -> R.string.r18_label
}
