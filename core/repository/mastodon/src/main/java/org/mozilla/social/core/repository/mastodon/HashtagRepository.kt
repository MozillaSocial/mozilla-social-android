package org.mozilla.social.core.repository.mastodon

import kotlinx.coroutines.flow.map
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.core.database.dao.HashTagsDao
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.network.mastodon.TagsApi
import org.mozilla.social.core.repository.mastodon.model.hashtag.toDatabaseModel
import org.mozilla.social.core.repository.mastodon.model.hashtag.toExternalModel

class HashtagRepository(
    private val dao: HashTagsDao,
    private val api: TagsApi,
) {

    fun insertAll(
        hashTags: List<HashTag>
    ) = dao.upsertAll(hashTags.map { it.toDatabaseModel() })

    fun insert(
        hashTag: HashTag,
    ) = dao.upsert(hashTag.toDatabaseModel())

    fun getHashTagFlow(
        hashTag: String
    ) = dao.getHashTagFlow(hashTag).map { it.toExternalModel() }

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