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
    fun getPublicTimeline(): Flow<Page<List<Status>>?> =
        serviceWrapper.service.map { it?.getPublicTimeline() }

    fun getLocalTimeline(): Flow<Page<List<Status>>?> =
        serviceWrapper.service.map { it?.getLocalTimeline() }

    suspend fun retrieveHomeTimeline(): Response<List<org.mozilla.social.model.entity.Status>> {
        return mastodonApi.getHomeTimeline()
    }
}