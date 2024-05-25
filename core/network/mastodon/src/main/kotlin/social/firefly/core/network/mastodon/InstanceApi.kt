package social.firefly.core.network.mastodon

import retrofit2.http.GET
import social.firefly.core.network.mastodon.model.responseBody.NetworkExtendedDescription
import social.firefly.core.network.mastodon.model.responseBody.NetworkInstance
import social.firefly.core.network.mastodon.model.responseBody.NetworkInstanceRule

interface InstanceApi {
    @GET("/api/v1/instance/rules")
    suspend fun getRules(): List<NetworkInstanceRule>

    @GET("/api/v2/instance")
    suspend fun getInstance(): NetworkInstance

    @GET("/api/v1/instance/extended_description")
    suspend fun getExtendedDescription(): NetworkExtendedDescription
}
