package org.mozilla.social.core.data.repository.model.context

import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.network.mastodon.model.NetworkContext
import org.mozilla.social.model.Context

fun NetworkContext.toExternalModel(): Context =
    Context(
        ancestors = ancestors.map { it.toExternalModel() },
        descendants = descendants.map { it.toExternalModel() }
    )