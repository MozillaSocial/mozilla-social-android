package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.responseBody.NetworkRelationship

interface FollowRequestApi {

    suspend fun acceptFollowRequest(
        accountId: String,
    ): NetworkRelationship

    suspend fun rejectFollowRequest(
        accountId: String,
    ): NetworkRelationship
}