package org.mozilla.social.core.data.repository.model.instance

import org.mozilla.social.core.network.model.NetworkInstanceRule
import org.mozilla.social.core.network.model.NetworkInstanceRules
import org.mozilla.social.model.InstanceRule

fun NetworkInstanceRules.toExternalModel(): List<InstanceRule> =
    rules.map { it.toExternalModel() }

fun NetworkInstanceRule.toExternalModel(): InstanceRule =
    InstanceRule(
        id = id,
        text = text,
    )