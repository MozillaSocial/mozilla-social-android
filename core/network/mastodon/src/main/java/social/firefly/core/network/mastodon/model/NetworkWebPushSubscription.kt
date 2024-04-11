package social.firefly.core.network.mastodon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkWebPushSubscription(
    @SerialName("id")
    val id: Int,
    @SerialName("endpoint")
    val endpoint: String,
    @SerialName("server_key")
    val serverKey: String,
    @SerialName("alerts")
    val alerts: NetworkAlerts,
)

@Serializable
data class NetworkAlerts(
    @SerialName("mention")
    val mention: Boolean,
    @SerialName("status")
    val status: Boolean,
    @SerialName("reblog")
    val reblog: Boolean,
    @SerialName("follow")
    val follow: Boolean,
    @SerialName("follow_request")
    val followRequest: Boolean,
    @SerialName("favourite")
    val favorite: Boolean,
    @SerialName("poll")
    val poll: Boolean,
    @SerialName("update")
    val update: Boolean,
    @SerialName("admin.sign_up")
    val adminSignUp: Boolean,
    @SerialName("admin.report")
    val adminReport: Boolean,
)