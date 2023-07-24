package org.mozilla.social.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.data.MastodonServiceWrapper
import org.mozilla.social.core.network.MastodonApi
import org.mozilla.social.model.Page
import org.mozilla.social.model.Status
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