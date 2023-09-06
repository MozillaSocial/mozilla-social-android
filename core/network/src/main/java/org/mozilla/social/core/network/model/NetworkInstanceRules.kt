package org.mozilla.social.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkInstanceRules(
    val rules: List<NetworkInstanceRule>
)