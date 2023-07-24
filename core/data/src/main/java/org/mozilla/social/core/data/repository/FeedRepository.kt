package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.MastodonServiceWrapper
import org.mozilla.social.core.network.MastodonApi
import retrofit2.Response

class FeedRepository internal constructor(
    private val serviceWrapper: MastodonServiceWrapper,
    private val mastodonApi: MastodonApi
) {
    suspend fun retrieveHomeTimeline(): Response<List<org.mozilla.social.model.entity.Status>> {
        return mastodonApi.getHomeTimeline()
    }

    suspend fun retrievePublicTimeline(): Response<List<org.mozilla.social.model.entity.Status>> {
        return mastodonApi.getPublicTimeline()
    }
}