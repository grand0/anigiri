package tech.bnuuy.anigiri.feature.player.api.data.model

data class Release(
    val id: Int,
    val name: String,
    val nameEng: String?,
    val year: Int,
    val isInProduction: Boolean,
    val isOngoing: Boolean,
    val posterUrl: String?,
    val thumbnailUrl: String?,
    val ageRatingLabel: String,
    val description: String,
    val episodesTotal: Int?,
    val episodeDurationMinutes: Int?,
    val favorites: Int,
    val genres: List<Genre>?,
    val episodes: List<Episode>?,
)
