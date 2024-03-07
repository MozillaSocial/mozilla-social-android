package social.firefly.core.model

data class WebPushSubscription(
    val id: Int,
    val endpoint: String,
    val serverKey: String,
    val alerts: Alerts,
)

data class Alerts(
    val mention: Boolean,
    val status: Boolean,
    val reblog: Boolean,
    val follow: Boolean,
    val followRequest: Boolean,
    val favorite: Boolean,
    val poll: Boolean,
    val update: Boolean,
    val adminSignUp: Boolean,
    val adminReport: Boolean,
)
