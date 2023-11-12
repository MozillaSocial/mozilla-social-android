package org.mozilla.social.core.storage.mastodon.timeline

import androidx.paging.PagingSource
import org.mozilla.social.core.database.dao.HashTagTimelineStatusDao
import org.mozilla.social.core.database.model.statusCollections.HashTagTimelineStatusWrapper

class LocalHashtagTimelineRepository(private val dao: HashTagTimelineStatusDao) {
    suspend fun deletePost(statusId: String) {
        dao.deletePost(statusId)
    }

    fun hashTagTimelinePagingSource(
        hashTag: String,
    ): PagingSource<Int, HashTagTimelineStatusWrapper> {
        return dao.hashTagTimelinePagingSource(hashTag)
    }
}