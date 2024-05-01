package social.firefly.core.repository.mastodon

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.database.dao.HashTagsDao
import social.firefly.core.model.HashTag
import social.firefly.core.network.mastodon.TagsApi
import social.firefly.core.repository.mastodon.model.hashtag.toDatabaseModel
import social.firefly.core.repository.mastodon.model.hashtag.toExternalModel

class HashtagRepository(
    private val dao: HashTagsDao,
    private val api: TagsApi,
) {

    suspend fun insertAll(
        hashTags: List<HashTag>
    ) = dao.upsertAll(hashTags.map { it.toDatabaseModel() })

    suspend fun insert(
        hashTag: HashTag,
    ) = dao.upsert(hashTag.toDatabaseModel())

    suspend fun deleteAll() = dao.deleteAll()

    fun getHashTagFlow(
        hashTag: String
    ): Flow<HashTag> = dao.getHashTagFlow(hashTag).map {
        it.toExternalModel()
    }

    @PreferUseCase
    suspend fun updateFollowing(
        hashTag: String,
        isFollowing: Boolean,
    ) = dao.updateFollowing(
        hashTag,
        isFollowing,
    )

    @PreferUseCase
    suspend fun followHashTag(
        hashTag: String,
    ): HashTag = api.followHashTag(hashTag).toExternalModel()

    @PreferUseCase
    suspend fun unfollowHashTag(
        hashTag: String,
    ): HashTag = api.unfollowHashTag(hashTag).toExternalModel()

    @PreferUseCase
    suspend fun getHashTag(
        hashTag: String
    ): HashTag = api.getHashTag(hashTag).toExternalModel()
}