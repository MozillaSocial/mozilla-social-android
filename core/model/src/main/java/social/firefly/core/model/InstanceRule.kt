package social.firefly.core.model

import kotlinx.serialization.Serializable

@Serializable
data class InstanceRule(
    val id: Int,
    val text: String,
)
