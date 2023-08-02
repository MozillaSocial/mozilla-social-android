package org.mozilla.social.core.network

import org.mozilla.social.core.network.model.NetworkAccount
import retrofit2.http.GET
import retrofit2.http.Path

interface AccountApi {
    @GET("/api/v1/accounts/{id}")
    suspend fun getAccount(
        @Path("id") accountId: String
    ): NetworkAccount
}