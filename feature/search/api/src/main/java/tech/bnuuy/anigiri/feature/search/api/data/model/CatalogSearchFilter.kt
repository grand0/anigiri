package tech.bnuuy.anigiri.feature.search.api.data.model

data class CatalogSearchFilter(
    val page: Int = 1,
    val limit: Int = 15,
    val genres: List<Int>? = null,
    val types: List<String>? = null,
    val seasons: List<String>? = null,
    val fromYear: Int? = null,
    val toYear: Int? = null,
    val search: String? = null,
    val sorting: String? = null,
    val ageRatings: List<String>? = null,
    val publishStatus: String? = null,
    val productionStatus: String? = null,
)
