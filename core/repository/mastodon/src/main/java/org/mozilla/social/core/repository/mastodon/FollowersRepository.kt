package org.mozilla.social.core.repository.mastodon

import androidx.paging.PagingSource
import org.mozilla.social.core.database.dao.FollowersDao
import org.mozilla.social.core.database.model.accountCollections.Follower
import org.mozilla.social.core.database.model.accountCollections.FollowerWrapper

class FollowersRepository(
    private val dao: FollowersDao,
) {

    fun getFollowersPagingSource(
        accountId: String,
    ): PagingSource<Int, FollowerWrapper> =
        dao.followersPagingSource(accountId)

    suspend fun deleteFollowers(
        accountId: String
    ) = dao.deleteFollowers(accountId)

    fun insertAll(followers: List<Follower>) =
        dao.insertAll(followers)
}