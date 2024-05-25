package social.firefly.core.network.mastodon

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus

interface FavoritesApi {
    @GET("/api/v1/favourites")
    suspend fun getFavorites(
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