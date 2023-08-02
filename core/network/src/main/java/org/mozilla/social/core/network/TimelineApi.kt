package org.mozilla.social.core.network

import org.mozilla.social.core.network.model.NetworkStatus
import retrofit2.http.GET

interface TimelineApi {
    @GET("/api/v1/timelines/home")
    suspend fun getHomeTimeline(): List<NetworkStatus>

    @GET("/api/v1/timelines/public")
    suspend fun getPublicTimeline(): List<NetworkStatus>
}