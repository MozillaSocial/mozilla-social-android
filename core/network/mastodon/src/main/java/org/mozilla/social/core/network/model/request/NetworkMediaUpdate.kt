package org.mozilla.social.core.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkMediaUpdate(
    @SerialName("description") val description: String,
)