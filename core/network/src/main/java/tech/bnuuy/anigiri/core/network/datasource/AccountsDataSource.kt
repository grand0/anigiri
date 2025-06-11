package tech.bnuuy.anigiri.core.network.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.bnuuy.anigiri.core.network.cache.FavoritesMemoryCache
import tech.bnuuy.anigiri.core.network.datasource.enumeration.AgeRating
import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType
import tech.bnuuy.anigiri.core.network.datasource.enumeration.ReleaseType
import tech.bnuuy.anigiri.core.network.datasource.enumeration.Season
import tech.bnuuy.anigiri.core.network.datasource.enumeration.Sorting
import tech.bnuuy.anigiri.core.network.datasource.request.AuthRequest
import tech.bnuuy.anigiri.core.network.datasource.request.CollectionReleaseId
import tech.bnuuy.anigiri.core.network.datasource.request.ReleaseId
import tech.bnuuy.anigiri.core.network.datasource.response.CollectionReleaseIdResponse
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
    suspend fun login(login: String, password: String): TokenResponse = withContext(Dispatchers.IO) {
        val resp = http.post(Accounts.Users.Auth.Login()) {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(login, password))
        }
        when (resp.status) {
            HttpStatusCode.OK -> resp.body()
            HttpStatusCode.Unauthorized, HttpStatusCode.UnprocessableEntity ->
                throw WrongCredentialsException()
            else -> throw UnknownException(resp.status.description)
        }
    }
    
    suspend fun logout(): TokenResponse = withContext(Dispatchers.IO) {
        val resp = http.postAuthenticated(Accounts.Users.Auth.Logout())
        when (resp.status) {
            HttpStatusCode.OK -> resp.body<TokenResponse>().also {
                favoritesMemoryCache.clear()
            }
            HttpStatusCode.Unauthorized -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }
    
    suspend fun myProfile(): ProfileResponse = withContext(Dispatchers.IO) {
        val resp = http.getAuthenticated(Accounts.Users.Me.Profile())
        when (resp.status) {
            HttpStatusCode.OK -> resp.body()
            HttpStatusCode.Forbidden, HttpStatusCode.NotFound -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }

    suspend fun favoriteReleases(
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
    ): MetaContentResponse<ReleaseResponse> = withContext(Dispatchers.IO) {
        val resp = http.getAuthenticated(Accounts.Users.Me.Favorites.Releases()) {
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
                }
            }
        }
        when (resp.status) {
            HttpStatusCode.OK -> resp.body()
            HttpStatusCode.Forbidden -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }
    
    suspend fun favoriteReleasesIds(): List<Int> = withContext(Dispatchers.IO) {
        if (favoritesMemoryCache.isFresh()) {
            return@withContext favoritesMemoryCache.getFavoriteReleases()
        }
        val resp = http.getAuthenticated(Accounts.Users.Me.Favorites.Ids())
        when (resp.status) {
            HttpStatusCode.OK -> resp.body<List<Int>>().also {
                favoritesMemoryCache.setFavoriteReleases(it)
            }

            HttpStatusCode.Forbidden -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }
    
    suspend fun addFavoriteReleases(releases: List<ReleaseId>) = withContext(Dispatchers.IO) {
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

    suspend fun removeFavoriteReleases(releases: List<ReleaseId>) = withContext(Dispatchers.IO) {
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

    suspend fun collectionReleases(
        page: Int = 1,
        limit: Int = 15,
        collectionType: CollectionType,
        genres: Set<Int>? = null,
        types: Set<ReleaseType>? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        search: String? = null,
        ageRatings: Set<AgeRating>? = null,
    ): MetaContentResponse<ReleaseResponse> = withContext(Dispatchers.IO) {
        val resp = http.getAuthenticated(Accounts.Users.Me.Collections.Releases()) {
            url {
                with(parameters) {
                    append("page", page.toString())
                    append("limit", limit.toString())
                    append("type_of_collection", collectionType.value)
                    genres?.let { append("f[genres]", genres.joinToString(",")) }
                    types?.let { append("f[types]", types.joinToString(",") { it.value }) }
                    fromYear?.let { append("f[years][from_year]", fromYear.toString()) }
                    toYear?.let { append("f[years][to_year]", toYear.toString()) }
                    search?.let { append("f[search]", search) }
                    ageRatings?.let { append("f[age_ratings]",
                        ageRatings.joinToString(",") { it.value }) }
                }
            }
        }
        when (resp.status) {
            HttpStatusCode.OK -> resp.body()
            HttpStatusCode.Forbidden -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }

    suspend fun collectionReleasesIds(): List<CollectionReleaseIdResponse> =
        withContext(Dispatchers.IO) {
            val resp = http.getAuthenticated(Accounts.Users.Me.Collections.Ids())
            when (resp.status) {
                HttpStatusCode.OK -> resp.body<List<List<Any>>>().map { elem ->
                    CollectionReleaseIdResponse(
                        id = elem[0] as Int,
                        collectionType = CollectionType.byValue(elem[1] as String)!!
                    )
                }
                HttpStatusCode.Forbidden -> throw NotAuthorizedException()
                else -> throw UnknownException(resp.status.description)
            }
        }

    suspend fun addToCollection(releases: List<CollectionReleaseId>) = withContext(Dispatchers.IO) {
        val resp = http.ignoringBrotli().postAuthenticated(Accounts.Users.Me.Collections()) {
            contentType(ContentType.Application.Json)
            setBody(releases)
        }
        when (resp.status) {
            HttpStatusCode.OK -> {}
            HttpStatusCode.Forbidden -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }

    suspend fun removeFromCollection(releases: List<ReleaseId>) = withContext(Dispatchers.IO) {
        val resp = http.ignoringBrotli().deleteAuthenticated(Accounts.Users.Me.Collections()) {
            contentType(ContentType.Application.Json)
            setBody(releases)
        }
        when (resp.status) {
            HttpStatusCode.OK -> {}
            HttpStatusCode.Forbidden -> throw NotAuthorizedException()
            else -> throw UnknownException(resp.status.description)
        }
    }
}
