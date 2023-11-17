package org.mozilla.social.core.repository.mastodon.model.context

import org.mozilla.social.core.model.Context
import org.mozilla.social.core.network.mastodon.model.NetworkContext
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

fun NetworkContext.toExternalModel(): Context =
    Context(
        ancestors = ancestors.map { it.toExternalModel() },
        descendants = descendants.map { it.toExternalModel() },
    )
