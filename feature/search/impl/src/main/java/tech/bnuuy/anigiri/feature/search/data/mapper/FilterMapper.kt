package tech.bnuuy.anigiri.feature.search.data.mapper

import tech.bnuuy.anigiri.feature.search.api.data.model.CatalogSearchFilter
import tech.bnuuy.anigiri.feature.search.data.model.CatalogSearchUiFilter

fun CatalogSearchUiFilter.toDomain() = CatalogSearchFilter(
    page = page,
    limit = limit,
    genres = genres.map { it.id },
    types = types.map { it.value },
    seasons = seasons.map { it.value },
    fromYear = fromYear,
    toYear = toYear,
    search = search,
    sorting = sorting.value,
    ageRatings = ageRatings.map { it.value },
    publishStatus = publishStatus?.value,
    productionStatus = productionStatus?.value,
)
