package org.mozilla.social.core.repository.mastodon.model.hashtag

import org.mozilla.social.core.database.model.entities.DatabaseHashTagEntity
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.repository.mastodon.model.status.toDatabaseModel

fun HashTag.toDatabaseModel(): DatabaseHashTagEntity = DatabaseHashTagEntity(
    name = name,
    url = url,
    history = history?.map { it.toDatabaseModel() },
    following = following,
)