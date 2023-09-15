package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.network.TimelineApi
import org.mozilla.social.model.Status

class TimelineRepository internal constructor(
    private val timelineApi: TimelineApi,
) {

    suspend fun getHomeTimeline(
        olderThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        loadSize: Int? = null,
    ): List<Status> =
        timelineApi.getHomeTimeline(
            olderThanId = olderThanId,
            immediatelyNewerThanId = immediatelyNewerThanId,
            limit = loadSize,
        ).map { it.toExternalModel() }

    suspend fun getPublicTimeline(): List<Status> =
        timelineApi.getPublicTimeline().map { it.toExternalModel() }

    suspend fun getHashtagTimeline(
        hashTag: String,
        olderThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        loadSize: Int? = null,
    ): List<Status> =
        timelineApi.getHashTagTimeline(
            hashTag = hashTag,
            olderThanId = olderThanId,
            immediatelyNewerThanId = immediatelyNewerThanId,
            limit = loadSize,
        ).map { it.toExternalModel() }
}
