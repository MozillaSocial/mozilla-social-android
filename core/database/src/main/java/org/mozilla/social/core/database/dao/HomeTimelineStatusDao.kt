package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatusWrapper

@Dao
interface HomeTimelineStatusDao : BaseDao<HomeTimelineStatus> {
    @Query("SELECT * FROM homeTimeline")
    fun homeTimelinePagingSource(): PagingSource<Int, HomeTimelineStatusWrapper>

    @Query("DELETE FROM homeTimeline")
    fun deleteHomeTimeline()
}