package org.mozilla.social.core.repository.mastodon

import androidx.paging.PagingSource
import org.mozilla.social.core.database.dao.FollowingsDao
import org.mozilla.social.core.database.model.accountCollections.Followee
import org.mozilla.social.core.database.model.accountCollections.FolloweeWrapper

class FollowingsRepository(
    private val dao: FollowingsDao,
) {

    fun getFollowingsPagingSource(
        accountId: String,
    ): PagingSource<Int, FolloweeWrapper> =
        dao.followingsPagingSource(accountId)

    suspend fun deleteFollowings(
        accountId: String
    ) = dao.deleteFollowings(accountId)

    fun insertAll(followees: List<Followee>) =
        dao.insertAll(followees)
}