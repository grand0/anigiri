package tech.bnuuy.anigiri.core.network.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import tech.bnuuy.anigiri.core.network.datasource.response.MetaContentResponse
import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.core.network.resources.Anime

class AnimeDataSource(
    private val http: HttpClient,
) {
    
    suspend fun getRandomRelease(): List<ReleaseResponse> =
        http.get(Anime.Releases.Random()).body()

    suspend fun getLatestReleases(): List<ReleaseResponse> =
        http.get(Anime.Releases.Latest()).body()

    suspend fun getRelease(id: Int): ReleaseResponse =
        http.get(Anime.Releases.Id(id = id)).body()
    
    suspend fun searchCatalog(
        page: Int = 1,
        limit: Int = 15,
        genres: List<Int>? = null,
        types: List<String>? = null,
        seasons: List<String>? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        search: String? = null,
        sorting: String? = null,
        ageRatings: List<String>? = null,
        publishStatuses: String? = null,
        productionStatuses: String? = null,
    ): MetaContentResponse<ReleaseResponse> {
        return http.get(Anime.Catalog.Releases()) {
            url { 
                with(parameters) {
                    append("page", page.toString())
                    append("limit", limit.toString())
                    genres?.let { append("f[genres]", genres.joinToString(",")) }
                    types?.let { append("f[types]", types.joinToString(",")) }
                    seasons?.let { append("f[seasons]", seasons.joinToString(",")) }
                    fromYear?.let { append("f[years][from_year]", fromYear.toString()) }
                    toYear?.let { append("f[years][to_year]", toYear.toString()) }
                    search?.let { append("f[search]", search) }
                    sorting?.let { append("f[sorting]", sorting) }
                    ageRatings?.let { append("f[age_ratings]", ageRatings.joinToString(",")) }
                    publishStatuses?.let { append("f[publish_statuses]", publishStatuses) }
                    productionStatuses?.let { append("f[production_statuses]", productionStatuses) }
                }
            }
        }.body()
    }
}
