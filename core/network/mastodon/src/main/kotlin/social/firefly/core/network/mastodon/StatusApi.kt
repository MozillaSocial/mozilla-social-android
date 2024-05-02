package social.firefly.core.network.mastodon

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import social.firefly.core.network.mastodon.model.NetworkContext
import social.firefly.core.network.mastodon.model.NetworkPoll
import social.firefly.core.network.mastodon.model.NetworkStatus
import social.firefly.core.network.mastodon.model.request.NetworkPollVote
import social.firefly.core.network.mastodon.model.request.NetworkStatusCreate

interface StatusApi {
    @GET("/api/v1/statuses/{statusId}")
    suspend fun getStatus(
        @Path("statusId") statusId: String,
    ): NetworkStatus

    @POST("api/v1/statuses")
    suspend fun postStatus(
        @Body status: NetworkStatusCreate,
    ): NetworkStatus

    @PUT("api/v1/statuses/{statusId}")
    suspend fun editStatus(
        @Path("statusId") statusId: String,
        @Body status: NetworkStatusCreate,
    ): NetworkStatus

    @POST("/api/v1/polls/{pollId}/votes")
    suspend fun voteOnPoll(
        @Path("pollId") pollId: String,
        @Body body: NetworkPollVote,
    ): NetworkPoll

    @POST("/api/v1/statuses/{statusId}/reblog")
    suspend fun boostStatus(
        @Path("statusId") statusId: String,
    ): NetworkStatus

    @POST("/api/v1/statuses/{statusId}/unreblog")
    suspend fun unBoostStatus(
        @Path("statusId") statusId: String,
    ): NetworkStatus

    @POST("/api/v1/statuses/{statusId}/favourite")
    suspend fun favoriteStatus(
        @Path("statusId") statusId: String,
    ): NetworkStatus

    @POST("/api/v1/statuses/{statusId}/unfavourite")
    suspend fun unFavoriteStatus(
        @Path("statusId") statusId: String,
    ): NetworkStatus

    @GET("/api/v1/statuses/{statusId}/context")
    suspend fun getStatusContext(
        @Path("statusId") statusId: String,
    ): NetworkContext

    @DELETE("/api/v1/statuses/{statusId}")
    suspend fun deleteStatus(
        @Path("statusId") statusId: String,
    )
}
