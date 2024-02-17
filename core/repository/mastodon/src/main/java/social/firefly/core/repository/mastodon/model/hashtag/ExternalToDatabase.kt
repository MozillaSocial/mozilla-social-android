package social.firefly.core.repository.mastodon.model.hashtag

import social.firefly.core.database.model.entities.DatabaseHashTagEntity
import social.firefly.core.model.HashTag
import social.firefly.core.repository.mastodon.model.status.toDatabaseModel

fun HashTag.toDatabaseModel(): DatabaseHashTagEntity = DatabaseHashTagEntity(
    name = name,
    url = url,
    history = history?.map { it.toDatabaseModel() },
    following = following,
)