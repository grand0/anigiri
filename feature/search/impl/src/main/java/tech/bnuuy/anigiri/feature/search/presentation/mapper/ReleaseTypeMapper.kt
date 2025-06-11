package tech.bnuuy.anigiri.feature.search.presentation.mapper

import androidx.annotation.StringRes
import tech.bnuuy.anigiri.core.network.datasource.enumeration.ReleaseType
import tech.bnuuy.anigiri.feature.search.R

@StringRes
fun ReleaseType.presentationNameId(): Int = when (this) {
    ReleaseType.TV -> R.string.tv_label
    ReleaseType.ONA -> R.string.ona_label
    ReleaseType.WEB -> R.string.web_label
    ReleaseType.OVA -> R.string.ova_label
    ReleaseType.OAD -> R.string.oad_label
    ReleaseType.MOVIE -> R.string.movie_label
    ReleaseType.DORAMA -> R.string.dorama_label
    ReleaseType.SPECIAL -> R.string.special_label
}
