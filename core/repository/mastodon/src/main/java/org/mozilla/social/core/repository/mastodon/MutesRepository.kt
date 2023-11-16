package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.network.mastodon.MutesApi
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

class MutesRepository(private val api: MutesApi) {
    suspend fun getMutes() = api.getMutes().map { it.toExternalModel() }
}