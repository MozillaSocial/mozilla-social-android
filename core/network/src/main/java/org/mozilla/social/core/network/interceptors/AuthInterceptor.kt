package org.mozilla.social.core.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    var accessToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        accessToken?.let { builder.addHeader("authorization", "Bearer $it") }

        return chain.proceed(builder.build())
    }
}