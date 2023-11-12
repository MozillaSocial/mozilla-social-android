package org.mozilla.social.core.model

import kotlinx.serialization.Serializable

@Serializable
data class InstanceRule(
    val id: Int,
    val text: String,
)