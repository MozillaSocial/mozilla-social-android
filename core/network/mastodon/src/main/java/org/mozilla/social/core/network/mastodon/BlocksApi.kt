package org.mozilla.social.core.network.mastodon

import org.mozilla.social.core.network.mastodon.model.NetworkAccount
import retrofit2.http.GET

interface BlocksApi {
    @GET("/api/v1/blocks")
    suspend fun getBlocks(): List<NetworkAccount>
}

