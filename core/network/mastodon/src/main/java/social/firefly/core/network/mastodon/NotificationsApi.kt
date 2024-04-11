package social.firefly.core.network.mastodon

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import social.firefly.core.network.mastodon.model.NetworkNotification

interface NotificationsApi {

    @GET("/api/v1/notifications")
    suspend fun getNotifications(
        @Query("max_id") maxId: String? = null,
        @Query("since_id") sinceId: String? = null,
        @Query("min_id") minId: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("types[]") types: Array<String>? = null,
        @Query("exclude_types[]") excludeTypes: Array<String>? = null,
        @Query("account_id") accountId: String? = null,
    ): Response<List<NetworkNotification>>
}