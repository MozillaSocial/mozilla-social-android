package social.firefly.core.network.mastodon

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import social.firefly.core.network.mastodon.model.NetworkStatus

interface BookmarksApi {

    @GET("/api/v1/bookmarks")
    suspend fun getBookmarks(
        @Query("max_id") maxId: String? = null,
        @Query("since_id") sinceId: String? = null,
        @Query("min_id") minId: String? = null,
        @Query("limit") limit: Int? = null,
    ): Response<List<NetworkStatus>>
}