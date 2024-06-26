package social.firefly.core.network.mastodon

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import social.firefly.core.model.exceptions.HttpException
import social.firefly.core.network.mastodon.interceptors.AuthCredentialInterceptor
import social.firefly.core.network.mastodon.ktor.AccountApiImpl
import social.firefly.core.network.mastodon.ktor.AppApiImpl
import social.firefly.core.network.mastodon.ktor.BlocksApiImpl
import social.firefly.core.network.mastodon.ktor.BookmarksApiImpl
import social.firefly.core.network.mastodon.ktor.DomainBlocksApiImpl
import social.firefly.core.network.mastodon.ktor.FavoritesApiImpl
import social.firefly.core.network.mastodon.ktor.FollowRequestApiImpl
import social.firefly.core.network.mastodon.ktor.FollowedTagsApiImpl
import social.firefly.core.network.mastodon.ktor.InstanceApiImpl
import social.firefly.core.network.mastodon.ktor.MediaApiImpl
import social.firefly.core.network.mastodon.ktor.MutesApiImpl
import social.firefly.core.network.mastodon.ktor.NotificationsApiImpl
import social.firefly.core.network.mastodon.ktor.PushApiImpl
import social.firefly.core.network.mastodon.ktor.ReportApiImpl
import social.firefly.core.network.mastodon.ktor.SearchApiImpl
import social.firefly.core.network.mastodon.ktor.StatusApiImpl
import social.firefly.core.network.mastodon.ktor.TagsApiImpl
import social.firefly.core.network.mastodon.ktor.TimelineApiImpl
import social.firefly.core.network.mastodon.ktor.TrendsApiImpl
import social.firefly.core.network.mastodon.model.responseBody.NetworkError
import java.util.concurrent.TimeUnit

val mastodonNetworkModule =
    module {
        single { AuthCredentialInterceptor() }

        single<HttpClientEngineFactory<OkHttpConfig>> {
            OkHttp
        }

        single<HttpClientConfig<HttpClientEngineConfig>> {
            HttpClientConfig<HttpClientEngineConfig>().apply {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
                expectSuccess = true
                HttpResponseValidator {
                    handleResponseExceptionWithRequest { cause, _ ->
                        if (cause !is ClientRequestException) return@handleResponseExceptionWithRequest

                        val errorMessage: NetworkError = cause.response.body()

                        throw HttpException(
                            code = cause.response.status.value,
                            errorMessage = errorMessage.error,
                            cause = cause,
                        )
                    }
                }
            }
        }

        single<HttpClientEngine>(
            named(UNAUTHORIZED_CLIENT)
        ) {
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

        single(
            named(UNAUTHORIZED_CLIENT)
        ) {
            HttpClient(
                get<HttpClientEngine>(qualifier = named(UNAUTHORIZED_CLIENT)),
                get<HttpClientConfig<HttpClientEngineConfig>>(),
            )
        }

        single<HttpClientEngine>(
            named(AUTHORIZED_CLIENT)
        ) {
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
                addInterceptor(get<AuthCredentialInterceptor>())
            }
        }

        single(
            named(AUTHORIZED_CLIENT)
        ) {
            HttpClient(
                get<HttpClientEngine>(qualifier = named(AUTHORIZED_CLIENT)),
                get<HttpClientConfig<HttpClientEngineConfig>>(),
            )
        }

        // Unauthorized Apis
        single<AppApi> { AppApiImpl(get(qualifier = named(UNAUTHORIZED_CLIENT))) }

        // Authorized Apis
        single<AccountApi> { AccountApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<BlocksApi> { BlocksApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<BookmarksApi> { BookmarksApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<DomainBlocksApi> { DomainBlocksApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<FavoritesApi> { FavoritesApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<FollowedTagsApi> { FollowedTagsApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<FollowRequestApi> { FollowRequestApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<InstanceApi> { InstanceApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<MediaApi> { MediaApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<MutesApi> { MutesApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<NotificationsApi> { NotificationsApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<PushApi> { PushApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<ReportApi> { ReportApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<SearchApi> { SearchApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<StatusApi> { StatusApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<TagsApi> { TagsApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<TimelineApi> { TimelineApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
        single<TrendsApi> { TrendsApiImpl(get(qualifier = named(AUTHORIZED_CLIENT))) }
    }

private const val AUTHORIZED_CLIENT = "authorizedClient"
private const val UNAUTHORIZED_CLIENT = "unauthorizedClient"
private const val OKHTTP_TIMEOUT = 30L
