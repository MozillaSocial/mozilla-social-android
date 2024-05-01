package social.firefly.core.repository.paging.pagers

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.hashtagCollections.FollowedHashTag
import social.firefly.core.database.model.entities.hashtagCollections.FollowedHashTagWrapper
import social.firefly.core.model.HashTag
import social.firefly.core.model.PageItem
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.FollowedHashTagsRepository
import social.firefly.core.repository.mastodon.HashtagRepository
import social.firefly.core.repository.mastodon.model.hashtag.toExternalModel
import social.firefly.core.repository.paging.IdBasedPager

class FollowedHashTagsPager(
    private val hashtagRepository: HashtagRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val followedHashTagsRepository: FollowedHashTagsRepository,
) : IdBasedPager<HashTag, FollowedHashTagWrapper> {
    override fun mapDbObjectToExternalModel(dbo: FollowedHashTagWrapper): HashTag =
        dbo.hashTag.toExternalModel()

    override suspend fun saveLocally(items: List<PageItem<HashTag>>) {
        databaseDelegate.withTransaction {
            hashtagRepository.insertAll(items.map { it.item })
            followedHashTagsRepository.insertAll(
                items.map { item ->
                    FollowedHashTag(
                        hashTagName = item.item.name,
                        position = item.position,
                    )
                }
            )
        }
    }

    override suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<HashTag> =
        followedHashTagsRepository.getFollowedHashTagsPage(
            maxId = nextKey,
            limit = limit,
        )

    override fun pagingSource(): PagingSource<Int, FollowedHashTagWrapper> =
        followedHashTagsRepository.pagingSource()
}