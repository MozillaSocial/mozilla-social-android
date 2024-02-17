package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import social.firefly.core.database.model.entities.accountCollections.SearchedAccount
import social.firefly.core.database.model.entities.accountCollections.SearchedAccountWrapper
import social.firefly.core.database.model.entities.hashtagCollections.SearchedHashTag
import social.firefly.core.database.model.entities.hashtagCollections.SearchedHashTagWrapper
import social.firefly.core.database.model.entities.statusCollections.SearchedStatus
import social.firefly.core.database.model.entities.statusCollections.SearchedStatusWrapper

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
        "DELETE FROM searchedAccounts "
    )
    suspend fun deleteAllSearchedAccounts()

    @Query(
        "DELETE FROM searchedStatuses "
    )
    suspend fun deleteAllSearchedStatues()

    @Query(
        "DELETE FROM searchedHashTags "
    )
    suspend fun deleteAllSearchedHashTags()

    @Upsert
    suspend fun upsertAccounts(accounts: List<SearchedAccount>)

    @Upsert
    suspend fun upsertStatuses(statuses: List<SearchedStatus>)

    @Upsert
    suspend fun upsertHashTags(hashTags: List<SearchedHashTag>)

    @Transaction
    @Query(
        "SELECT * FROM searchedAccounts " +
                "LIMIT :count"
    )
    fun getTopAccountsFlow(count: Int): Flow<List<SearchedAccountWrapper>>

    @Transaction
    @Query(
        "SELECT * FROM searchedStatuses " +
                "LIMIT :count"
    )
    fun getTopStatusesFlow(count: Int): Flow<List<SearchedStatusWrapper>>

    @Transaction
    @Query(
        "SELECT * FROM searchedHashTags " +
                "LIMIT :count"
    )
    fun getTopHashTagsFlow(count: Int): Flow<List<SearchedHashTagWrapper>>
}