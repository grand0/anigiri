package tech.bnuuy.anigiri.core.network.model

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
    val genres: List<Genre>?,
    val members: Map<String, List<String>>?,
    
    // context-based fields
    val latestEpisode: Episode?,
    val episodes: List<Episode>?,
)
