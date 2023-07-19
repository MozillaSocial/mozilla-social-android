package org.mozilla.social.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.data.MastodonServiceWrapper
import org.mozilla.social.model.Page
import org.mozilla.social.model.Status

class FeedRepository internal constructor(private val serviceWrapper: MastodonServiceWrapper) {
    fun getPublicTimeline(): Flow<Page<List<Status>>?> =
        serviceWrapper.service.map { it?.getPublicTimeline() }
}