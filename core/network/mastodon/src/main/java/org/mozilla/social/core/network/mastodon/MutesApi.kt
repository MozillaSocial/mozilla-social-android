package org.mozilla.social.core.network.mastodon

import org.mozilla.social.core.network.mastodon.model.NetworkAccount
import retrofit2.http.GET

interface MutesApi {
    @GET("/api/v1/mutes")
    suspend fun getMutes(): List<NetworkAccount>
}