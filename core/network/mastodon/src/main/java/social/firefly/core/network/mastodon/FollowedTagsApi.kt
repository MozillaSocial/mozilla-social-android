package social.firefly.core.network.mastodon

import retrofit2.http.GET
import retrofit2.http.Query
import social.firefly.core.network.mastodon.model.NetworkHashTag

interface FollowedTagsApi {

    @GET("/api/v1/followed_tags")
    suspend fun getFollowedHashTags(
        @Query("max_id") olderThanId: String? = null,
        @Query("since_id") newerThanId: String? = null,
        @Query("min_id") immediatelyNewerThanId: String? = null,
        @Query("limit") limit: Int? = null,
    ): List<NetworkHashTag>
}