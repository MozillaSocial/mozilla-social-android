package social.firefly.core.network.mozilla

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import social.firefly.core.network.mozilla.ktor.RecommendationApiImpl
import java.util.concurrent.TimeUnit

val mozillaNetworkModule = module {

    single<HttpClientEngineFactory<OkHttpConfig>> {
        OkHttp
    }

    single<HttpClientEngine> {
        get<HttpClientEngineFactory<OkHttpConfig>>().create {
            config {
                readTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                connectTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
            }

            addNetworkInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BASIC
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
        }
    }

    single<HttpClientConfig<HttpClientEngineConfig>> {
        HttpClientConfig<HttpClientEngineConfig>().apply {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            expectSuccess = true
            defaultRequest {
                url("https://mozilla.social/")
            }
        }
    }

    single {
        HttpClient(
            get<HttpClientEngine>(),
            get<HttpClientConfig<HttpClientEngineConfig>>(),
        )
    }

    single<RecommendationApi> { RecommendationApiImpl(get()) }
}

private const val OKHTTP_TIMEOUT = 30L
