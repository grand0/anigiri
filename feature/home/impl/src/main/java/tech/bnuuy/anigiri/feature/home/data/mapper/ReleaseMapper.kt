package tech.bnuuy.anigiri.feature.home.data.mapper

import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.parseUrl
import kotlinx.datetime.Instant
import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.feature.home.BuildConfig
import tech.bnuuy.anigiri.feature.home.api.data.model.Release

internal fun List<ReleaseResponse>.listToDomain(): List<Release> = map {
    it.toDomain()
}

internal fun ReleaseResponse.toDomain(): Release {
    val srcUrl = posters.srcUrl?.let {
        URLBuilder(parseUrl(BuildConfig.ANILIBRIA_STORAGE_BASE_URL)!!)
            .appendPathSegments(it)
            .toString()
    }
    val thumbUrl = posters.thumbnailUrl?.let {
        URLBuilder(parseUrl(BuildConfig.ANILIBRIA_STORAGE_BASE_URL)!!)
            .appendPathSegments(it)
            .toString()
    }

    return Release(
        id = id,
        name = names.main,
        nameEng = names.english,
        isInProduction = isInProduction,
        isOngoing = isOngoing,
        posterUrl = srcUrl,
        thumbnailUrl = thumbUrl,
        ageRatingLabel = ageRating.label,
        description = description,
        latestEpisodePublishTime = latestEpisode?.let { Instant.parse(it.updatedAt) }
    )
}
