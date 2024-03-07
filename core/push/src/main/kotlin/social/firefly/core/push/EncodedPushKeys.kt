package social.firefly.core.push

import kotlinx.serialization.Serializable

@Serializable
data class EncodedPushKeys(
    val privateKey: String,
    val publicKey: String,
    val authSecret: String,
)
