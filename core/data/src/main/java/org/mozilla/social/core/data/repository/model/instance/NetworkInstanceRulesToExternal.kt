package org.mozilla.social.core.data.repository.model.instance

import org.mozilla.social.core.network.model.NetworkInstanceRule
import org.mozilla.social.model.InstanceRule

fun NetworkInstanceRule.toExternalModel(): InstanceRule =
    InstanceRule(
        id = id,
        text = text,
    )

fun List<NetworkInstanceRule>.toExternalModel(): List<InstanceRule> =
    map { it.toExternalModel() }