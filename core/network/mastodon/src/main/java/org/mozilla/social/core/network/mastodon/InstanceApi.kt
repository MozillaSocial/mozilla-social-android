package org.mozilla.social.core.network.mastodon

import org.mozilla.social.core.network.mastodon.model.NetworkExtendedDescription
import org.mozilla.social.core.network.mastodon.model.NetworkInstance
import org.mozilla.social.core.network.mastodon.model.NetworkInstanceRule
import retrofit2.http.GET

interface InstanceApi {
    @GET("/api/v1/instance/rules")
    suspend fun getRules(): List<NetworkInstanceRule>

    @GET("/api/v2/instance")
    suspend fun getInstance(): NetworkInstance

    @GET("/api/v1/instance/extended_description")
    suspend fun getExtendedDescription(): NetworkExtendedDescription
}
