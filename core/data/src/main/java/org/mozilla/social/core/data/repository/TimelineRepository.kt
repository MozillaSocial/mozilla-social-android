package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.repository.model.toExternalModel
import org.mozilla.social.core.network.TimelineApi
import org.mozilla.social.model.Status

class TimelineRepository internal constructor(private val timelineApi: TimelineApi) {
    suspend fun getHomeTimeline(
        olderThanId: String? = null,
        immediatelyNewerThanId: String? = null,
    ): List<Status> =
        timelineApi.getHomeTimeline(
            olderThanId = olderThanId,
            immediatelyNewerThanId = immediatelyNewerThanId,
        ).map { it.toExternalModel() }

    suspend fun getPublicTimeline(): List<Status> =
        timelineApi.getPublicTimeline().map { it.toExternalModel() }
}
