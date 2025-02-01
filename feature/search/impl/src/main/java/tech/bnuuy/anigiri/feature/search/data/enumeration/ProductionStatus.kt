package tech.bnuuy.anigiri.feature.search.data.enumeration

import androidx.annotation.StringRes
import tech.bnuuy.anigiri.feature.search.R

enum class ProductionStatus(
    val value: String,
    @StringRes val labelResId: Int,
) {
    IS_IN_PRODUCTION(
        value = "IS_IN_PRODUCTION",
        labelResId = R.string.in_production_label,
    ),
    IS_NOT_IN_PRODUCTION(
        value = "IS_NOT_IN_PRODUCTION",
        labelResId = R.string.not_in_production_label,
    ),
}
