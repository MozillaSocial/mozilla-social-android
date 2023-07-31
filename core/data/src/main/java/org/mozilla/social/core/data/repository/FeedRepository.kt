package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.MastodonServiceWrapper
import org.mozilla.social.core.network.MastodonApi
import org.mozilla.social.model.entity.Status
import retrofit2.Response

class FeedRepository internal constructor(
    private val serviceWrapper: MastodonServiceWrapper,
    private val mastodonApi: MastodonApi
) {
    suspend fun retrieveHomeTimeline(): Response<List<Status>> {
        return mastodonApi.getHomeTimeline()
    }

    suspend fun retrievePublicTimeline(): Response<List<Status>> {
        return mastodonApi.getPublicTimeline()
    }
}