package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.responseBody.NetworkWebPushSubscription

interface PushApi {

    suspend fun subscribe(
        endpoint: String,
        p256dh: String,
        auth: String,
        mention: Boolean = true,
        status: Boolean = true,
        reblog: Boolean = true,
        follow: Boolean = true,
        followRequest: Boolean = true,
        favourite: Boolean = true,
        poll: Boolean = true,
        update: Boolean = true,
        adminSignUp: Boolean = false,
        adminReport: Boolean = false,
        policy: String = "all"
    ): NetworkWebPushSubscription

    suspend fun getSubscription(): NetworkWebPushSubscription
}