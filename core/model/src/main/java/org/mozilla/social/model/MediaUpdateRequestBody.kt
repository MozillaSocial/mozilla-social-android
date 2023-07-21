package org.mozilla.social.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaUpdateRequestBody(
    @SerialName("description") val description: String,
)