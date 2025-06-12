package tech.bnuuy.anigiri.core.network.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.bnuuy.anigiri.core.network.datasource.enumeration.AgeRating
import tech.bnuuy.anigiri.core.network.datasource.enumeration.ProductionStatus
import tech.bnuuy.anigiri.core.network.datasource.enumeration.PublishStatus
import tech.bnuuy.anigiri.core.network.datasource.enumeration.ReleaseType
import tech.bnuuy.anigiri.core.network.datasource.enumeration.Season
import tech.bnuuy.anigiri.core.network.datasource.enumeration.Sorting
import tech.bnuuy.anigiri.core.network.datasource.response.EpisodeResponse
import tech.bnuuy.anigiri.core.network.datasource.response.GenreResponse
import tech.bnuuy.anigiri.core.network.datasource.response.MetaContentResponse
import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.core.network.datasource.response.ScheduleReleaseResponse
import tech.bnuuy.anigiri.core.network.resources.Anime

class AnimeDataSource(
    private val http: HttpClient,
) {
    
    suspend fun getRandomRelease(): List<ReleaseResponse> = withContext(Dispatchers.IO) {
        http.get(Anime.Releases.Random()).body()
    }

    suspend fun getLatestReleases(): List<ReleaseResponse> = withContext(Dispatchers.IO) {
        http.get(Anime.Releases.Latest()).body()
    }

    suspend fun getRelease(id: Int): ReleaseResponse = withContext(Dispatchers.IO) {
        http.get(Anime.Releases.Id(id = id)).body()
    }
    
    suspend fun searchCatalog(
        page: Int = 1,
        limit: Int = 15,
        genres: Set<Int>? = null,
        types: Set<ReleaseType>? = null,
        seasons: Set<Season>? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        search: String? = null,
        sorting: Sorting? = null,
        ageRatings: Set<AgeRating>? = null,
        publishStatus: PublishStatus? = null,
        productionStatus: ProductionStatus? = null,
    ): MetaContentResponse<ReleaseResponse> = withContext(Dispatchers.IO) {
        http.get(Anime.Catalog.Releases()) {
            url {
                with(parameters) {
                    append("page", page.toString())
                    append("limit", limit.toString())
                    genres?.let { append("f[genres]", genres.joinToString(",")) }
                    types?.let { append("f[types]", types.joinToString(",") { it.value }) }
                    seasons?.let { append("f[seasons]", seasons.joinToString(",") { it.value }) }
                    fromYear?.let { append("f[years][from_year]", fromYear.toString()) }
                    toYear?.let { append("f[years][to_year]", toYear.toString()) }
                    search?.let { append("f[search]", search) }
                    sorting?.let { append("f[sorting]", sorting.value) }
                    ageRatings?.let { append("f[age_ratings]",
                        ageRatings.joinToString(",") { it.value }) }
                    publishStatus?.let { append("f[publish_statuses]", publishStatus.value) }
                    productionStatus?.let { append("f[production_statuses]",
                        productionStatus.value) }
                }
            }
        }.body()
    }
    
    suspend fun catalogGenres(): List<GenreResponse> = withContext(Dispatchers.IO) { 
        http.get(Anime.Catalog.References.Genres()).body()
    }
    
    suspend fun catalogYears(): List<Int> = withContext(Dispatchers.IO) {
        http.get(Anime.Catalog.References.Years()).body()
    }

    suspend fun getEpisode(id: String): EpisodeResponse = withContext(Dispatchers.IO) {
        http.get(Anime.Releases.Episodes.Id(id = id)).body()
    }

    suspend fun getWeekSchedule(): List<ScheduleReleaseResponse> = withContext(Dispatchers.IO) {
        http.get(Anime.Schedule.Week()).body()
    }
}
