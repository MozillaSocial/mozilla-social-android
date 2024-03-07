package social.firefly.core.repository.mastodon.model.push

import social.firefly.core.model.Alerts
import social.firefly.core.model.WebPushSubscription
import social.firefly.core.network.mastodon.model.NetworkAlerts
import social.firefly.core.network.mastodon.model.NetworkWebPushSubscription

fun NetworkWebPushSubscription.toExternal(): WebPushSubscription = WebPushSubscription(
    id = id,
    endpoint = endpoint,
    serverKey = serverKey,
    alerts = alerts.toExternal(),
)

fun NetworkAlerts.toExternal(): Alerts = Alerts(
    mention = mention,
    status = status,
    reblog = reblog,
    follow = follow,
    followRequest = followRequest,
    favorite = favorite,
    poll = poll,
    update = update,
    adminSignUp = adminSignUp,
    adminReport = adminReport
)
