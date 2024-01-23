package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.core.model.Relationship
import org.mozilla.social.core.network.mastodon.FollowRequestApi
import org.mozilla.social.core.repository.mastodon.model.account.toExternal

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