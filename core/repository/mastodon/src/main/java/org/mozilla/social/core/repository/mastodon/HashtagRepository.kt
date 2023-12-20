package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.database.dao.HashTagsDao
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.repository.mastodon.model.hashtag.toDatabaseModel

class HashtagRepository(
    private val hashTagsDao: HashTagsDao,
) {

    fun insertAll(
        hashTags: List<HashTag>
    ) {
        hashTagsDao.upsertAll(hashTags.map { it.toDatabaseModel() })
    }
}