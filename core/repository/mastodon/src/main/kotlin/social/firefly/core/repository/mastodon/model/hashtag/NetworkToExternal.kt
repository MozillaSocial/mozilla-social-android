package social.firefly.core.repository.mastodon.model.hashtag

import social.firefly.core.model.HashTag
import social.firefly.core.network.mastodon.model.responseBody.NetworkHashTag
import social.firefly.core.repository.mastodon.model.status.toExternalModel

fun NetworkHashTag.toExternalModel(): HashTag =
    HashTag(
        name = name,
        url = url,
        history = history?.map { it.toExternalModel() },
        following = following,
    )
