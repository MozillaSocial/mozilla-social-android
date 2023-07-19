package org.mozilla.social.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single {
        val contentType: MediaType = MediaType.get("application/json")

        Retrofit.Builder()
            .baseUrl("https://mozilla.social")
            .addConverterFactory(json.asConverterFactory(contentType = contentType))
            .build()
    }
    single { get<Retrofit>().create(AuthService::class.java) }
}

private var json: Json = Json { ignoreUnknownKeys = true }