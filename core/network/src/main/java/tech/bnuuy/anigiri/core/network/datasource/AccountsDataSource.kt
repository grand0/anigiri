package tech.bnuuy.anigiri.core.network.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import tech.bnuuy.anigiri.core.network.cache.FavoritesMemoryCache
import tech.bnuuy.anigiri.core.network.datasource.request.AuthRequest
import tech.bnuuy.anigiri.core.network.datasource.request.ReleaseId
import tech.bnuuy.anigiri.core.network.datasource.response.MetaContentResponse
import tech.bnuuy.anigiri.core.network.datasource.response.ProfileResponse
import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.core.network.datasource.response.TokenResponse
import tech.bnuuy.anigiri.core.network.encoder.ignoringBrotli
import tech.bnuuy.anigiri.core.network.exception.NotAuthorizedException
import tech.bnuuy.anigiri.core.network.exception.UnknownException
import tech.bnuuy.anigiri.core.network.exception.WrongCredentialsException
import tech.bnuuy.anigiri.core.network.resources.Accounts
import tech.bnuuy.anigiri.core.network.util.deleteAuthenticated
import tech.bnuuy.anigiri.core.network.util.getAuthenticated
import tech.bnuuy.anigiri.core.network.util.postAuthenticated

class AccountsDataSource(
    private val http: HttpClient,
    private val favoritesMemoryCache: FavoritesMemoryCache,
) {
    suspend fun login(login: String, password: String): TokenResponse {
        val resp = http.post(Accounts.Users.Auth.Login()) {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(login, password))
        }
        when (resp.status) {
            HttpStatusCode.OK -> return resp.body()
            HttpStatusCode.Unauthorized, HttpStatusCode.UnprocessableEntity -> 
                throw WrongCredentialsException()
            else -> throw UnknownException(resp.status.description)
        }
    }
    
    suspend fun logout(): TokenResponse {
        val resp = http.postAuthenticated(Accounts.Users.Auth.Logout())
        when (resp.status) {
            HttpStatusCode.OK -> return resp.body<TokenResponse>().also {
                favoritesMemoryCache.clear()
            }
            HttpStatusCode.Unauthorized -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }
    
    suspend fun myProfile(): ProfileResponse {
        val resp = http.getAuthenticated(Accounts.Users.Me.Profile())
        when (resp.status) {
            HttpStatusCode.OK -> return resp.body()
            HttpStatusCode.Forbidden, HttpStatusCode.NotFound -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }

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
        val resp = http.getAuthenticated(Accounts.Users.Me.Favorites.Releases()) {
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
        }
        when (resp.status) {
            HttpStatusCode.OK -> return resp.body()
            HttpStatusCode.Forbidden -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }
    
    suspend fun favoriteReleasesIds(): List<Int> {
        if (favoritesMemoryCache.isFresh()) {
            return favoritesMemoryCache.getFavoriteReleases()
        }
        val resp = http.getAuthenticated(Accounts.Users.Me.Favorites.Ids())
        when (resp.status) {
            HttpStatusCode.OK -> return resp.body<List<Int>>().also {
                favoritesMemoryCache.setFavoriteReleases(it)
            }
            HttpStatusCode.Forbidden -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }
    
    suspend fun addFavoriteReleases(releases: List<ReleaseId>) {
        val resp = http.ignoringBrotli().postAuthenticated(Accounts.Users.Me.Favorites()) {
            contentType(ContentType.Application.Json)
            setBody(releases)
        }
        when (resp.status) {
            HttpStatusCode.OK ->
                favoritesMemoryCache.addFavoriteReleases(releases.map { it.releaseId })
            HttpStatusCode.Forbidden -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }

    suspend fun removeFavoriteReleases(releases: List<ReleaseId>) {
        val resp = http.ignoringBrotli().deleteAuthenticated(Accounts.Users.Me.Favorites()) {
            contentType(ContentType.Application.Json)
            setBody(releases)
        }
        when (resp.status) {
            HttpStatusCode.OK ->
                favoritesMemoryCache.removeFavoriteReleases(releases.map { it.releaseId })
            HttpStatusCode.Forbidden -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }
}
