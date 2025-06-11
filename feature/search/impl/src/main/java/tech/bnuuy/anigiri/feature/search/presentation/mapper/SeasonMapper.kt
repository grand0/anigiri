package tech.bnuuy.anigiri.feature.search.presentation.mapper

import androidx.annotation.StringRes
import tech.bnuuy.anigiri.core.network.datasource.enumeration.Season
import tech.bnuuy.anigiri.feature.search.R

@StringRes
fun Season.presentationNameId(): Int = when (this) {
    Season.WINTER -> R.string.winter_label
    Season.SPRING -> R.string.spring_label
    Season.SUMMER -> R.string.summer_label
    Season.AUTUMN -> R.string.autumn_label
}
