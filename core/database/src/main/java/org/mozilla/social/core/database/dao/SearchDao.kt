package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.mozilla.social.core.database.model.entities.accountCollections.SearchedAccount
import org.mozilla.social.core.database.model.entities.accountCollections.SearchedAccountWrapper
import org.mozilla.social.core.database.model.entities.hashtagCollections.SearchedHashTag
import org.mozilla.social.core.database.model.entities.hashtagCollections.SearchedHashTagWrapper
import org.mozilla.social.core.database.model.entities.statusCollections.SearchedStatus
import org.mozilla.social.core.database.model.entities.statusCollections.SearchedStatusWrapper
import org.mozilla.social.core.database.model.wrappers.SearchResultWrapper

@Dao
interface SearchDao {
    @Transaction
    @Query(
        "SELECT * FROM searchedAccounts " +
        "ORDER BY position ASC",
    )
    fun accountsPagingSource(): PagingSource<Int, SearchedAccountWrapper>

    @Transaction
    @Query(
        "SELECT * FROM searchedStatuses " +
        "ORDER BY position ASC",
    )
    fun statusesPagingSource(): PagingSource<Int, SearchedStatusWrapper>

    @Transaction
    @Query(
        "SELECT * FROM searchedHashTags " +
        "ORDER BY position ASC",
    )
    fun hashTagsPagingSource(): PagingSource<Int, SearchedHashTagWrapper>

    @Query(
        "DELETE FROM searchedAccounts ",
    )
    suspend fun deleteAllSearches()

    @Upsert
    fun upsertAccounts(accounts: List<SearchedAccount>)

    @Upsert
    fun upsertStatuses(statuses: List<SearchedStatus>)

    @Upsert
    fun upsertHashTags(hashTags: List<SearchedHashTag>)

    @Query(
        "SELECT * FROM searchedAccounts " +
        "UNION " +
        "SELECT * FROM searchedStatuses " +
        "UNION " +
        "SELECT * FROM searchedHashTags " +
        "LIMIT :count"
    )
    fun getTopResultsFlow(count: Int): Flow<SearchResultWrapper>
}