package social.firefly.core.repository.paging.pagers

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.hashtagCollections.TrendingHashTag
import social.firefly.core.database.model.entities.hashtagCollections.TrendingHashTagWrapper
import social.firefly.core.model.HashTag
import social.firefly.core.model.PageItem
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.HashtagRepository
import social.firefly.core.repository.mastodon.TrendingHashtagRepository
import social.firefly.core.repository.mastodon.model.hashtag.toExternalModel
import social.firefly.core.repository.paging.IndexBasedPager

class TrendingHashTagPager(
    private val trendingHashtagRepository: TrendingHashtagRepository,
    private val hashtagRepository: HashtagRepository,
    private val databaseDelegate: DatabaseDelegate,
) : IndexBasedPager<HashTag, TrendingHashTagWrapper> {

    override fun mapDbObjectToExternalModel(dbo: TrendingHashTagWrapper): HashTag =
        dbo.hashTag.toExternalModel()

    override fun pagingSource(): PagingSource<Int, TrendingHashTagWrapper> =
        trendingHashtagRepository.pagingSource()

    override suspend fun saveLocally(items: List<PageItem<HashTag>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) trendingHashtagRepository.deleteAll()
            hashtagRepository.insertAll(items.map { it.item })
            trendingHashtagRepository.insertAll(
                items.map { item ->
                    TrendingHashTag(
                        hashTagName = item.item.name,
                        position = item.position,
                    )
                }
            )
        }
    }

    override suspend fun getRemotely(limit: Int, offset: Int): List<HashTag> =
        trendingHashtagRepository.getTrendingTags(limit, offset)
}