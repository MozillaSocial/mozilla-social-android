package org.mozilla.social.core.network.mastodon

import org.mozilla.social.core.network.mastodon.model.NetworkSearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("/api/v2/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("type") type: String,
    ): NetworkSearchResult
}
