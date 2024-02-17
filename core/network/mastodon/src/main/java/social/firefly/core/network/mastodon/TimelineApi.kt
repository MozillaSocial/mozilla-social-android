package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.NetworkStatus
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TimelineApi {
    @GET("/api/v1/timelines/home")
    suspend fun getHomeTimeline(
        // Return results older than ID.
        @Query("max_id") olderThanId: String? = null,
        // Return results newer than ID.
        @Query("since_id") newerThanId: String? = null,
        // Return results immediately newer than ID.
        @Query("min_id") immediatelyNewerThanId: String? = null,
        // Maximum number of results to return. Defaults to 20 statuses. Max 40 statuses.
        @Query("limit") limit: Int? = null,
    ): Response<List<NetworkStatus>>

    @GET("/api/v1/timelines/public")
    suspend fun getPublicTimeline(
        @Query("local") localOnly: Boolean? = null,
        @Query("remote") federatedOnly: Boolean? = null,
        @Query("only_media") mediaOnly: Boolean? = null,
        // Return results older than ID.
        @Query("max_id") olderThanId: String? = null,
        // Return results newer than ID.
        @Query("since_id") newerThanId: String? = null,
        // Return results immediately newer than ID.
        @Query("min_id") immediatelyNewerThanId: String? = null,
        // Maximum number of results to return. Defaults to 20 statuses. Max 40 statuses.
        @Query("limit") limit: Int? = null,
    ): Response<List<NetworkStatus>>

    @GET("/api/v1/timelines/tag/{hashtag}")
    suspend fun getHashTagTimeline(
        @Path("hashtag") hashTag: String,
        // Return results older than ID.
        @Query("max_id") olderThanId: String? = null,
        // Return results newer than ID.
        @Query("since_id") newerThanId: String? = null,
        // Return results immediately newer than ID.
        @Query("min_id") immediatelyNewerThanId: String? = null,
        // Maximum number of results to return. Defaults to 20 statuses. Max 40 statuses.
        @Query("limit") limit: Int? = null,
    ): Response<List<NetworkStatus>>
}
