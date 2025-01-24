package tech.bnuuy.anigiri.core.network.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.core.network.resources.Anime

internal class AnimeDataSource(
    private val http: HttpClient,
) {
    
    suspend fun getRandomRelease(): List<ReleaseResponse> =
        http.get(Anime.Releases.Random()).body()

    suspend fun getLatestReleases(): List<ReleaseResponse> =
        http.get(Anime.Releases.Latest()).body()

    suspend fun getRelease(id: Int): ReleaseResponse =
        http.get(Anime.Releases.Id(id = id)).body()
}
