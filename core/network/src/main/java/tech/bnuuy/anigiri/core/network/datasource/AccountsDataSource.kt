package tech.bnuuy.anigiri.core.network.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import tech.bnuuy.anigiri.core.network.cache.FavoritesMemoryCache
import tech.bnuuy.anigiri.core.network.datasource.request.AuthRequest
import tech.bnuuy.anigiri.core.network.datasource.request.ReleaseId
import tech.bnuuy.anigiri.core.network.datasource.response.MetaContentResponse
import tech.bnuuy.anigiri.core.network.datasource.response.ProfileResponse
import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.core.network.datasource.response.TokenResponse
import tech.bnuuy.anigiri.core.network.encoder.ignoringBrotli
import tech.bnuuy.anigiri.core.network.resources.Accounts
import tech.bnuuy.anigiri.core.network.util.deleteAuthenticated
import tech.bnuuy.anigiri.core.network.util.getAuthenticated
import tech.bnuuy.anigiri.core.network.util.postAuthenticated

class AccountsDataSource(
    private val http: HttpClient,
    private val favoritesMemoryCache: FavoritesMemoryCache,
) {
    suspend fun login(login: String, password: String): TokenResponse =
        http.post(Accounts.Users.Auth.Login()) {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(login, password))
        }.body()
    
    suspend fun logout(): TokenResponse {
        return http.postAuthenticated(Accounts.Users.Auth.Logout()).body<TokenResponse>().also {
            favoritesMemoryCache.clear()
        }
    }
    
    suspend fun myProfile(): ProfileResponse =
        http.getAuthenticated(Accounts.Users.Me.Profile()).body()

    suspend fun favoriteReleases(
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
    ): MetaContentResponse<ReleaseResponse> {
        return http.getAuthenticated(Accounts.Users.Me.Favorites.Releases()) {
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
                }
            }
        }.body()
    }
    
    suspend fun favoriteReleasesIds(): List<Int> {
        if (favoritesMemoryCache.isFresh()) {
            return favoritesMemoryCache.getFavoriteReleases()
        }
        return http.getAuthenticated(Accounts.Users.Me.Favorites.Ids()).body<List<Int>>().also {
            favoritesMemoryCache.setFavoriteReleases(it)
        }
    }
    
    suspend fun addFavoriteReleases(releases: List<ReleaseId>) {
        http.ignoringBrotli().postAuthenticated(Accounts.Users.Me.Favorites()) {
            contentType(ContentType.Application.Json)
            setBody(releases)
        }.also { resp ->
            if (resp.status.isSuccess()) {
                favoritesMemoryCache.addFavoriteReleases(releases.map { it.releaseId })
            } else {
                throw ClientRequestException(resp, resp.body())
            }
        }
    }

    suspend fun removeFavoriteReleases(releases: List<ReleaseId>) {
        http.ignoringBrotli().deleteAuthenticated(Accounts.Users.Me.Favorites()) {
            contentType(ContentType.Application.Json)
            setBody(releases)
        }.also { resp ->
            if (resp.status.isSuccess()) {
                favoritesMemoryCache.removeFavoriteReleases(releases.map { it.releaseId })
            } else {
                throw ClientRequestException(resp, resp.body())
            }
        }
    }
}
