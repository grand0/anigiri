package tech.bnuuy.anigiri.feature.search.data.model

import tech.bnuuy.anigiri.feature.search.api.data.model.Genre
import tech.bnuuy.anigiri.feature.search.data.enumeration.AgeRating
import tech.bnuuy.anigiri.feature.search.data.enumeration.ProductionStatus
import tech.bnuuy.anigiri.feature.search.data.enumeration.PublishStatus
import tech.bnuuy.anigiri.feature.search.data.enumeration.ReleaseType
import tech.bnuuy.anigiri.feature.search.data.enumeration.Season
import tech.bnuuy.anigiri.feature.search.data.enumeration.Sorting

data class CatalogSearchUiFilter(
    val page: Int = 1,
    val limit: Int = 15,
    val genres: List<Genre> = emptyList(),
    val types: List<ReleaseType> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val fromYear: Int? = null,
    val toYear: Int? = null,
    val search: String = "",
    var sorting: Sorting = DEFAULT_SORTING,
    val ageRatings: List<AgeRating> = emptyList(),
    val publishStatus: PublishStatus? = null,
    val productionStatus: ProductionStatus? = null,
) {
    
    fun isDefault(
        minYear: Int? = null,
        maxYear: Int? = null,
    ): Boolean {
        return genres.isEmpty() &&
                types.isEmpty() &&
                seasons.isEmpty() &&
                (fromYear == null || fromYear == minYear) &&
                (toYear == null || toYear == maxYear) &&
                ageRatings.isEmpty() &&
                publishStatus == null &&
                productionStatus == null
    }
    
    companion object {
        val DEFAULT_SORTING = Sorting.UPDATED_DESC
    }
}
