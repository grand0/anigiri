package tech.bnuuy.anigiri.core.network.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.network.BuildConfig
import tech.bnuuy.anigiri.core.network.datasource.AccountsDataSource
import tech.bnuuy.anigiri.core.network.datasource.AnimeDataSource
import tech.bnuuy.anigiri.core.network.encoder.brotli
import tech.bnuuy.anigiri.core.network.session.AppSession

val networkModule = module {
    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                    coerceInputValues = true
                })
            }
            install(Resources)
            install(DefaultRequest)
            install(ContentEncoding) {
                brotli()
            }

//            if (BuildConfig.DEBUG) {
                install(Logging) {
                    logger = Logger.ANDROID
                    level = LogLevel.ALL
                }
//            }

            defaultRequest {
                url(BuildConfig.ANILIBRIA_BASE_URL)
            }
        }
    }
    
    singleOf(::AppSession)
    
    factoryOf(::AnimeDataSource)
    factoryOf(::AccountsDataSource)
}
