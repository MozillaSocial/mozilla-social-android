package social.firefly.core.repository.mastodon.model.context

import social.firefly.core.model.Context
import social.firefly.core.network.mastodon.model.NetworkContext
import social.firefly.core.repository.mastodon.model.status.toExternalModel

fun NetworkContext.toExternalModel(): Context =
    Context(
        ancestors = ancestors.map { it.toExternalModel() },
        descendants = descendants.map { it.toExternalModel() },
    )
