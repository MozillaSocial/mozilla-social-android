package social.firefly.core.network.mastodon

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import social.firefly.core.network.mastodon.model.NetworkAccount

interface BlocksApi {
    @GET("/api/v1/blocks")
    suspend fun getBlocks(
        @Query("max_id") maxId: String? = null,
        @Query("since_id") sinceId: String? = null,
        @Query("min_id") minId: String? = null,
        @Query("limit") limit: Int? = null,
    ): Response<List<NetworkAccount>>
}
