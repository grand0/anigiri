package tech.bnuuy.anigiri.feature.search.data.model

import tech.bnuuy.anigiri.core.network.datasource.enumeration.AgeRating
import tech.bnuuy.anigiri.core.network.datasource.enumeration.ProductionStatus
import tech.bnuuy.anigiri.core.network.datasource.enumeration.PublishStatus
import tech.bnuuy.anigiri.core.network.datasource.enumeration.ReleaseType
import tech.bnuuy.anigiri.core.network.datasource.enumeration.Season
import tech.bnuuy.anigiri.core.network.datasource.enumeration.Sorting
import tech.bnuuy.anigiri.feature.search.api.data.model.Genre
import tech.bnuuy.anigiri.feature.search.api.data.model.ICatalogSearchFilter

data class CatalogSearchFilter(
    val page: Int = 1,
    val limit: Int = 15,
    val genres: Set<Genre> = emptySet(),
    val types: Set<ReleaseType> = emptySet(),
    val seasons: Set<Season> = emptySet(),
    val fromYear: Int? = null,
    val toYear: Int? = null,
    val search: String = "",
    var sorting: Sorting = DEFAULT_SORTING,
    val ageRatings: Set<AgeRating> = emptySet(),
    val publishStatus: PublishStatus? = null,
    val productionStatus: ProductionStatus? = null,
) : ICatalogSearchFilter() {
    
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
