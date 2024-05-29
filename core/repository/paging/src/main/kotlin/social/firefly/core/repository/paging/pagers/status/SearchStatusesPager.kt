package social.firefly.core.repository.paging.pagers.status

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.statusCollections.SearchedStatus
import social.firefly.core.database.model.entities.statusCollections.SearchedStatusWrapper
import social.firefly.core.model.PageItem
import social.firefly.core.model.SearchType
import social.firefly.core.model.Status
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.SearchRepository
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.paging.IndexBasedPager
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class SearchStatusesPager(
    private val searchRepository: SearchRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
    private val query: String,
) : IndexBasedPager<Status, SearchedStatusWrapper> {

    override fun mapDbObjectToExternalModel(dbo: SearchedStatusWrapper): Status =
        dbo.status.toExternalModel()

    override suspend fun saveLocally(items: List<PageItem<Status>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            saveStatusToDatabase(items.map { it.item })
            searchRepository.insertAllStatuses(
                items.map {
                    SearchedStatus(
                        statusId = it.item.statusId,
                        position = it.position
                    )
                }
            )
        }
    }

    override suspend fun getRemotely(limit: Int, offset: Int): List<Status> {
        val statuses = searchRepository.search(
            query = query,
            type = SearchType.Statuses,
            limit = limit,
            offset = offset,
        ).statuses

        return getInReplyToAccountNames(statuses)
    }


    override fun pagingSource(): PagingSource<Int, SearchedStatusWrapper> =
        searchRepository.getStatusesPagingSource()
}