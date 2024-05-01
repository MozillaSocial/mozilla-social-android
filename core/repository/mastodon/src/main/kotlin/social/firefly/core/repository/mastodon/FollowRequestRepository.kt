package social.firefly.core.repository.mastodon

import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.model.Relationship
import social.firefly.core.network.mastodon.FollowRequestApi
import social.firefly.core.repository.mastodon.model.account.toExternal

class FollowRequestRepository(
    private val api: FollowRequestApi,
) {

    @PreferUseCase
    suspend fun acceptFollowRequest(
        accountId: String,
    ): Relationship = api.acceptFollowRequest(accountId).toExternal()

    @PreferUseCase
    suspend fun rejectFollowRequest(
        accountId: String,
    ): Relationship = api.rejectFollowRequest(accountId).toExternal()
}