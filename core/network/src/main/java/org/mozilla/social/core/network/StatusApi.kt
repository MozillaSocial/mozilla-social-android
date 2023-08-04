package org.mozilla.social.core.network

import org.mozilla.social.core.network.model.NetworkPoll
import org.mozilla.social.core.network.model.request.NetworkStatusCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface StatusApi {
    @POST("api/v1/statuses")
    suspend fun postStatus(
        @Body status: NetworkStatusCreate
    )

    /**
     * @param choices Array of Integer. Provide your own votes as an index for each option (starting from 0).
     */
    @FormUrlEncoded
    @POST("/api/v1/polls/{pollId}/votes")
    suspend fun voteOnPoll(
        @Path("pollId") pollId: String,
        @Field("choices") choices: List<Int>,
    ): NetworkPoll
}