package tech.bnuuy.anigiri.feature.schedule.data.mapper

import tech.bnuuy.anigiri.core.network.datasource.response.ScheduleReleaseResponse
import tech.bnuuy.anigiri.core.network.util.buildStorageUrl
import tech.bnuuy.anigiri.feature.schedule.api.data.model.Release

fun List<ScheduleReleaseResponse>.mapList(): List<Release> = map {
    it.toDomain()
}

fun ScheduleReleaseResponse.toDomain(): Release = release.run {
    val srcUrl = posters.srcUrl?.let { buildStorageUrl(it) }
    val thumbUrl = posters.thumbnailUrl?.let { buildStorageUrl(it) }

    Release(
        id = id,
        name = names.main,
        nameEng = names.english,
        isInProduction = isInProduction,
        isOngoing = isOngoing,
        posterUrl = srcUrl,
        thumbnailUrl = thumbUrl,
        ageRatingLabel = ageRating.label,
        description = description,
        publishDay = publishDay?.value ?: 0, // use case should probably remove releases with 0
    )
}
