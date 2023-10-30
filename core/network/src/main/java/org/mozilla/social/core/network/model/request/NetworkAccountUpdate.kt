package org.mozilla.social.core.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAccountUpdate(
    @SerialName("display_name")
    val displayName: String? = null,
    @SerialName("note")
    val bio: String? = null,
)