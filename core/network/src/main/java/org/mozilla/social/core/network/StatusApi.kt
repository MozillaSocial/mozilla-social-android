package org.mozilla.social.core.network

import org.mozilla.social.core.network.model.request.NetworkStatusCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface StatusApi {
    @POST("api/v1/statuses")
    suspend fun postStatus(
        @Body status: NetworkStatusCreate
    ): Response<Unit>
}