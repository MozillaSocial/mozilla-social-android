package social.firefly.core.network.mastodon

import retrofit2.http.GET
import retrofit2.http.Query
import social.firefly.core.network.mastodon.model.responseBody.NetworkMarker

interface MarkersApi {

    @GET("/api/v1/markers")
    suspend fun getMarkers(
        @Query("timeline[]") timelines: Array<String>?,
    ): NetworkMarker
}