package tech.bnuuy.anigiri.core.network.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val names: ReleaseNameResponse,
    @SerialName("year")
    val year: Int,
    @SerialName("is_in_production")
    val isInProduction: Boolean,
    @SerialName("is_ongoing")
    val isOngoing: Boolean,
    @SerialName("poster")
    val posters: ReleasePosterResponse,
    @SerialName("description")
    val description: String = "",
    @SerialName("age_rating")
    val ageRating: ReleaseAgeRatingResponse,
    @SerialName("episodes_total")
    val episodesTotal: Int?,
    @SerialName("average_duration_of_episode")
    val episodeDurationMinutes: Int?,
    @SerialName("added_in_users_favorites")
    val favorites: Int,
    @SerialName("genres")
    val genres: List<GenreResponse>?,
    @SerialName("members")
    val members: List<ReleaseMemberResponse>?,
    
    // context-based fields
    @SerialName("latest_episode")
    val latestEpisode: EpisodeResponse?,
    @SerialName("episodes")
    val episodes: List<EpisodeResponse>?,
) {

    @Serializable
    data class ReleaseNameResponse(
        val main: String,
        val english: String?,
        val alternative: String?,
    )

    @Serializable
    data class ReleasePosterResponse(
        @SerialName("src")
        val srcUrl: String?,
        @SerialName("thumbnail")
        val thumbnailUrl: String?,
    )

    @Serializable
    data class ReleaseAgeRatingResponse(
        @SerialName("label")
        val label: String,
    )

    @Serializable
    data class ReleaseMemberResponse(
        @SerialName("nickname")
        val name: String,
        @SerialName("role")
        val role: ReleaseMemberRoleResponse,
    ) {

        @Serializable
        data class ReleaseMemberRoleResponse(
            @SerialName("description")
            val name: String,
        )
    }
}
