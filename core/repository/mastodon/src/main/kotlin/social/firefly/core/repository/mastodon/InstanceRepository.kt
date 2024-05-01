package social.firefly.core.repository.mastodon

import social.firefly.core.model.Instance
import social.firefly.core.model.InstanceRule
import social.firefly.core.network.mastodon.InstanceApi
import social.firefly.core.repository.mastodon.model.instance.toExternalModel

class InstanceRepository(
    private val instanceApi: InstanceApi,
) {
    suspend fun getInstanceRules(): List<InstanceRule> = instanceApi.getRules().toExternalModel()

    suspend fun getInstance(): Instance = instanceApi.getInstance().toExternalModel()

    suspend fun getExtendedDescription() = instanceApi.getExtendedDescription().content
}
