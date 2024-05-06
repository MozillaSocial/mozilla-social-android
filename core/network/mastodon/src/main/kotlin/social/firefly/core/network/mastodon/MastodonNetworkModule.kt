package social.firefly.core.network.mastodon

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import social.firefly.core.network.mastodon.interceptors.AuthCredentialInterceptor
import java.util.concurrent.TimeUnit

val mastodonNetworkModule =
    module {
        single { AuthCredentialInterceptor() }
        single(
            named(AUTHORIZED_CLIENT),
        ) {
            OkHttpClient.Builder()
                .readTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) {
                            HttpLoggingInterceptor.Level.BASIC
                        } else {
                            HttpLoggingInterceptor.Level.NONE
                        }
                    },
                )
                .addInterceptor(get<AuthCredentialInterceptor>())
                .build()
        }
        single(
            named(AUTHORIZED_CLIENT)
        ) {
            Retrofit.Builder()
                .baseUrl("https://mozilla.social/")
                .client(get(qualifier = named(AUTHORIZED_CLIENT)))
                .addConverterFactory(
                    json.asConverterFactory(
                        contentType = "application/json".toMediaType()
                    )
                )
                .build()
        }

        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(AccountApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(AppApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(BlocksApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(FavoritesApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(InstanceApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(MediaApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(MutesApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(ReportApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(SearchApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(StatusApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(TimelineApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(TagsApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(NotificationsApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(FollowRequestApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(TrendsApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(PushApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(FollowedTagsApi::class.java) }
        single { get<Retrofit>(qualifier = named(AUTHORIZED_CLIENT)).create(BookmarksApi::class.java) }

        factory(
            named(VERIFICATION_CLIENT),
        ) {
            OkHttpClient.Builder()
                .readTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) {
                            HttpLoggingInterceptor.Level.BASIC
                        } else {
                            HttpLoggingInterceptor.Level.NONE
                        }
                    },
                )
                .build()
        }
        factory(
            named(VERIFICATION_CLIENT)
        ) { parametersHolder ->
            Retrofit.Builder()
                .baseUrl(parametersHolder.get<String>())
                .client(get(qualifier = named(VERIFICATION_CLIENT)))
                .addConverterFactory(
                    json.asConverterFactory(
                        contentType = "application/json".toMediaType()
                    )
                )
                .build()
        }

        factory(
            named(VERIFICATION_CLIENT)
        ) { parametersHolder ->
            get<Retrofit>(qualifier = named(VERIFICATION_CLIENT)) {
                parametersOf(
                    parametersHolder.get<String>()
                )
            }.create(AppApi::class.java)
        }
    }

private var json: Json = Json { ignoreUnknownKeys = true }
private const val AUTHORIZED_CLIENT = "authorizedClient"
private const val OKHTTP_TIMEOUT = 30L
const val VERIFICATION_CLIENT = "verificationClient"
