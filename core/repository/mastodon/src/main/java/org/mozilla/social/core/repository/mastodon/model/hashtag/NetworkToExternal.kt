package org.mozilla.social.core.repository.mastodon.model.hashtag

import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.network.mastodon.model.NetworkHashTag
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

fun NetworkHashTag.toExternalModel(): HashTag = HashTag(
    name = name,
    url = url,
    history = history?.map { it.toExternalModel() },
    following = following,
)
