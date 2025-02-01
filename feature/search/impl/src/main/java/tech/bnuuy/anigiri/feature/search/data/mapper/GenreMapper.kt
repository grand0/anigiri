package tech.bnuuy.anigiri.feature.search.data.mapper

import tech.bnuuy.anigiri.core.network.datasource.response.GenreResponse
import tech.bnuuy.anigiri.feature.search.api.data.model.Genre

fun List<GenreResponse>.mapToDomain(): List<Genre> = map { it.toDomain() }

fun GenreResponse.toDomain(): Genre {
    return Genre(
        id = id,
        name = name,
    )
}
