package org.mozilla.social.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.mozilla.social.core.network.interceptors.AuthInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

fun networkModule(isDebug: Boolean) = module {
    single {
        val contentType: MediaType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl("https://mozilla.social")
            .addConverterFactory(json.asConverterFactory(contentType = contentType))
            .build()
    }
    single { get<Retrofit>().create(AuthService::class.java) }
    single { AuthInterceptor() }
    single(
        named(AUTHORIZED_CLIENT)
    ) {
        OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = if (isDebug) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://mozilla.social")
            .client(get(qualifier = named(AUTHORIZED_CLIENT)))
            .addConverterFactory(json.asConverterFactory(contentType = "application/json".toMediaType()))
            .build()
            .create(MastodonApi::class.java)
    }
}

private var json: Json = Json { ignoreUnknownKeys = true }
private const val AUTHORIZED_CLIENT = "authorizedClient"