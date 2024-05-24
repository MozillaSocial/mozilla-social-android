package social.firefly.core.network.mastodon

import retrofit2.http.POST
import retrofit2.http.Path
import social.firefly.core.network.mastodon.model.responseBody.NetworkRelationship

interface FollowRequestApi {

    @POST("/api/v1/follow_requests/{accountId}/authorize")
    suspend fun acceptFollowRequest(
        @Path("accountId") accountId: String,
    ): NetworkRelationship

    @POST("/api/v1/follow_requests/{accountId}/reject")
    suspend fun rejectFollowRequest(
        @Path("accountId") accountId: String,
    ): NetworkRelationship
}