package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.model.Instance
import org.mozilla.social.core.model.InstanceRule
import org.mozilla.social.core.network.mastodon.InstanceApi
import org.mozilla.social.core.repository.mastodon.model.instance.toExternalModel

class InstanceRepository(
    private val instanceApi: InstanceApi,
) {
    suspend fun getInstanceRules(): List<InstanceRule> = instanceApi.getRules().toExternalModel()

    suspend fun getInstance(): Instance = instanceApi.getInstance().toExternalModel()

    suspend fun getExtendedDescription() = instanceApi.getExtendedDescription().content
}
