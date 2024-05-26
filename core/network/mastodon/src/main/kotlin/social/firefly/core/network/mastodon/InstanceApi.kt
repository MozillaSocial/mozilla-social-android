package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.responseBody.NetworkExtendedDescription
import social.firefly.core.network.mastodon.model.responseBody.NetworkInstance
import social.firefly.core.network.mastodon.model.responseBody.NetworkInstanceRule

interface InstanceApi {
    suspend fun getRules(): List<NetworkInstanceRule>

    suspend fun getInstance(): NetworkInstance

    suspend fun getExtendedDescription(): NetworkExtendedDescription
}
