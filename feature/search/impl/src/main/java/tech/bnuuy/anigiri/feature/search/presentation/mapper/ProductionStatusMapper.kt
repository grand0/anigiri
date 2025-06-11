package tech.bnuuy.anigiri.feature.search.presentation.mapper

import androidx.annotation.StringRes
import tech.bnuuy.anigiri.core.network.datasource.enumeration.ProductionStatus
import tech.bnuuy.anigiri.feature.search.R

@StringRes
fun ProductionStatus.presentationNameId(): Int = when (this) {
    ProductionStatus.IS_IN_PRODUCTION -> R.string.in_production_label
    ProductionStatus.IS_NOT_IN_PRODUCTION -> R.string.not_in_production_label
}