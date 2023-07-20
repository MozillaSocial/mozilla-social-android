package org.mozilla.social.core.network

import org.mozilla.social.model.entity.request.StatusCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MastodonApi {

    @POST("api/v1/statuses")
    suspend fun postStatus(
        @Body status: StatusCreate
    ): Response<Unit>
}