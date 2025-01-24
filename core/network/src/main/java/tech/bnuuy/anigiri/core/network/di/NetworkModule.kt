package tech.bnuuy.anigiri.core.network.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.network.BuildConfig
import tech.bnuuy.anigiri.core.network.datasource.AnimeDataSource
import tech.bnuuy.anigiri.core.network.repository.AnimeRepository

val networkModule = module {
    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }
            install(Resources)
            install(DefaultRequest)

            if (BuildConfig.DEBUG) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
            }

            defaultRequest {
                url(BuildConfig.ANILIBRIA_BASE_URL)
            }
        }
    }
    
    factoryOf(::AnimeDataSource)
    factoryOf(::AnimeRepository)
}
