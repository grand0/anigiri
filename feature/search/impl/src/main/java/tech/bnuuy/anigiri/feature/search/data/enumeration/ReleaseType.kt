package tech.bnuuy.anigiri.feature.search.data.enumeration

import androidx.annotation.StringRes
import tech.bnuuy.anigiri.feature.search.R

enum class ReleaseType(
    val value: String,
    @StringRes val labelResId: Int,
) {
    TV(
        value = "TV",
        labelResId = R.string.tv_label,
    ),
    ONA(
        value = "ONA",
        labelResId = R.string.ona_label,
    ),
    WEB(
        value = "WEB",
        labelResId = R.string.web_label,
    ),
    OVA(
        value = "OVA",
        labelResId = R.string.ova_label,
    ),
    OAD(
        value = "OAD",
        labelResId = R.string.oad_label,
    ),
    MOVIE(
        value = "MOVIE",
        labelResId = R.string.movie_label,
    ),
    DORAMA(
        value = "DORAMA",
        labelResId = R.string.dorama_label,
    ),
    SPECIAL(
        value = "SPECIAL",
        labelResId = R.string.special_label,
    ),
}
