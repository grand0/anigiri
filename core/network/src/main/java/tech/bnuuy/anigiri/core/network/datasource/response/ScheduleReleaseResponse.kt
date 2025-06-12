package tech.bnuuy.anigiri.core.network.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleReleaseResponse(
    @SerialName("release")
    val release: ReleaseResponse,
    @SerialName("full_season_is_released")
    val fullSeasonIsReleased: Boolean?,
    @SerialName("published_release_episode")
    val publishReleaseEpisode: EpisodeResponse?,
    @SerialName("next_release_episode_number")
    val nextReleaseEpisodeNumber: Int?,
)
