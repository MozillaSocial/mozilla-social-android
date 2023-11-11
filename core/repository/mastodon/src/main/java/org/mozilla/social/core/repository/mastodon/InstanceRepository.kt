package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.repository.mastodon.model.instance.toExternalModel
import org.mozilla.social.core.network.mastodon.InstanceApi
import org.mozilla.social.model.Instance
import org.mozilla.social.model.InstanceRule

class InstanceRepository(
    private val instanceApi: InstanceApi,
) {
    suspend fun getInstanceRules(): List<InstanceRule> =
        instanceApi.getRules().toExternalModel()

    suspend fun getInstance(): Instance =
        instanceApi.getInstance().toExternalModel()
}