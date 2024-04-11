package social.firefly.core.repository.mastodon.model.hashtag

import social.firefly.core.database.model.entities.DatabaseHashTagEntity
import social.firefly.core.model.HashTag
import social.firefly.core.repository.mastodon.model.status.toExternalModel

fun DatabaseHashTagEntity.toExternalModel(): HashTag = HashTag(
    name = name,
    url = url,
    history = history?.map { it.toExternalModel() },
    following = following,
)