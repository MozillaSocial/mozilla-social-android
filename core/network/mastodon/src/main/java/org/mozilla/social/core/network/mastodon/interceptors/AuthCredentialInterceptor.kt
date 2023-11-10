package org.mozilla.social.core.network.mastodon.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthCredentialInterceptor : Interceptor {

    var accessToken: String? = null
    var domain: String =
        DEFAULT_HOST // only non-null if different domain

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        builder.url(request.url.newBuilder().host(domain).build())

        builder.addHeader("Authorization", "Bearer $accessToken")



        return chain.proceed(builder.build())
    }

    companion object {
        const val DEFAULT_HOST = "mozilla.social"
    }
}