package social.firefly.core.repository.paging.pagers.hashTags

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.hashtagCollections.SearchedHashTag
import social.firefly.core.database.model.entities.hashtagCollections.SearchedHashTagWrapper
import social.firefly.core.model.HashTag
import social.firefly.core.model.PageItem
import social.firefly.core.model.SearchType
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.HashtagRepository
import social.firefly.core.repository.mastodon.SearchRepository
import social.firefly.core.repository.mastodon.model.hashtag.toExternalModel
import social.firefly.core.repository.paging.IndexBasedPager

class SearchHashTagPager(
    private val searchRepository: SearchRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val hashtagRepository: HashtagRepository,
    private val query: String,
) : IndexBasedPager<HashTag, SearchedHashTagWrapper> {

    override fun mapDbObjectToExternalModel(dbo: SearchedHashTagWrapper): HashTag =
        dbo.hashTag.toExternalModel()

    override suspend fun saveLocally(items: List<PageItem<HashTag>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            hashtagRepository.insertAll(items.map { it.item })
            searchRepository.insertAllHashTags(
                items.map { item ->
                    SearchedHashTag(
                        hashTagName = item.item.name,
                        position = item.position,
                    )
                }
            )
        }
    }

    override suspend fun getRemotely(limit: Int, offset: Int): List<HashTag> =
        searchRepository.search(
            query = query,
            type = SearchType.Hashtags,
            limit = limit,
            offset = offset,
        ).hashtags

    override fun pagingSource(): PagingSource<Int, SearchedHashTagWrapper> =
        searchRepository.getHashTagPagingSource()
}