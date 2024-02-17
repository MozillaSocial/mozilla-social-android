package social.firefly.core.network.mastodon

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import social.firefly.core.network.mastodon.interceptors.AuthCredentialInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val mastodonNetworkModule =
    module {
        single { AuthCredentialInterceptor() }
        single(
            named(social.firefly.core.network.mastodon.AUTHORIZED_CLIENT),
        ) {
            OkHttpClient.Builder()
                .readTimeout(social.firefly.core.network.mastodon.OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(social.firefly.core.network.mastodon.OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(
                    HttpLoggingInterceptor().apply {
                        level =
                            if (BuildConfig.DEBUG) {
                                HttpLoggingInterceptor.Level.BASIC
                            } else {
                                HttpLoggingInterceptor.Level.NONE
                            }
                    },
                )
                .addInterceptor(get<AuthCredentialInterceptor>())
                .build()
        }
        single {
            Retrofit.Builder()
                .baseUrl("https://mozilla.social/")
                .client(get(qualifier = named(social.firefly.core.network.mastodon.AUTHORIZED_CLIENT)))
                .addConverterFactory(social.firefly.core.network.mastodon.json.asConverterFactory(contentType = "application/json".toMediaType()))
                .build()
        }

        single { get<Retrofit>().create(social.firefly.core.network.mastodon.AccountApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.AppApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.BlocksApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.FavoritesApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.InstanceApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.MediaApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.MutesApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.OauthApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.ReportApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.SearchApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.StatusApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.TimelineApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.TagsApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.NotificationsApi::class.java) }
        single { get<Retrofit>().create(social.firefly.core.network.mastodon.FollowRequestApi::class.java) }
    }

private var json: Json = Json { ignoreUnknownKeys = true }
private const val AUTHORIZED_CLIENT = "authorizedClient"
private const val OKHTTP_TIMEOUT = 30L
