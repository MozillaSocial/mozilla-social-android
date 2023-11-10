package org.mozilla.social.core.network.mozilla

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

fun mozillaNetworkModule(isDebug: Boolean) = module {
    single(
        named(RECCS_SERVICE)
    ) {
        Retrofit.Builder()
            .baseUrl("https://mozilla.social/")
            .client(get(qualifier = named(RECCS_CLIENT)))
            .addConverterFactory(json.asConverterFactory(contentType = "application/json".toMediaType()))
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://mozilla.social/")
            .client(get(qualifier = named(AUTHORIZED_CLIENT)))
            .addConverterFactory(json.asConverterFactory(contentType = "application/json".toMediaType()))
            .build()
    }
    single { get<Retrofit>().create(RecommendationApi::class.java) }
}

private var json: Json = Json { ignoreUnknownKeys = true }
private const val AUTHORIZED_CLIENT = "authorizedClient"
private const val RECCS_CLIENT = "reccsClient"
private const val RECCS_SERVICE = "reccsService"
