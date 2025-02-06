package tech.bnuuy.anigiri.feature.favorites.api.data.model

data class Release(
    val id: Int,
    val name: String,
    val nameEng: String?,
    val isInProduction: Boolean,
    val isOngoing: Boolean,
    val posterUrl: String?,
    val thumbnailUrl: String?,
    val ageRatingLabel: String,
    val description: String,
)
