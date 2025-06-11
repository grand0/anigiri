package tech.bnuuy.anigiri.feature.search.presentation.mapper

import androidx.annotation.StringRes
import tech.bnuuy.anigiri.core.network.datasource.enumeration.Sorting
import tech.bnuuy.anigiri.feature.search.R

@StringRes
fun Sorting.presentationNameId(): Int = when (this) {
    Sorting.UPDATED_DESC -> R.string.updated_desc_label
    Sorting.UPDATED_ASC -> R.string.updated_asc_label
    Sorting.RATING_DESC -> R.string.rating_desc_label
    Sorting.RATING_ASC -> R.string.rating_asc_label
    Sorting.YEAR_DESC -> R.string.year_desc_label
    Sorting.YEAR_ASC -> R.string.year_asc_label
}
