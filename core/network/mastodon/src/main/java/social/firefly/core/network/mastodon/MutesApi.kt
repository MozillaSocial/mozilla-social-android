package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.NetworkAccount
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MutesApi {
    @GET("/api/v1/mutes")
    suspend fun getMutes(
        @Query("max_id") maxId: String? = null,
        @Query("since_id") sinceId: String? = null,
        @Query("min_id") minId: String? = null,
        @Query("limit") limit: Int? = null,
    ): Response<List<NetworkAccount>>
}
