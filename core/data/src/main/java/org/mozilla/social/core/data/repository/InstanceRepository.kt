package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.repository.model.instance.toExternalModel
import org.mozilla.social.core.network.InstanceApi
import org.mozilla.social.model.InstanceRule

class InstanceRepository(
    private val instanceApi: InstanceApi,
) {
    suspend fun getInstanceRules(): List<InstanceRule> =
        instanceApi.getRules().toExternalModel()
}