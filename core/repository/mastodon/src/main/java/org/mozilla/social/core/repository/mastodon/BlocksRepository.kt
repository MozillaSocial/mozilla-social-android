package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.network.mastodon.BlocksApi
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

class BlocksRepository(private val api: BlocksApi) {
    suspend fun getBlocks() = api.getBlocks().map { it.toExternalModel() }
}