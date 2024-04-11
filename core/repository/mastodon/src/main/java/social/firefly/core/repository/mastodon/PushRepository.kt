package social.firefly.core.repository.mastodon

import social.firefly.core.model.WebPushSubscription
import social.firefly.core.network.mastodon.PushApi
import social.firefly.core.repository.mastodon.model.push.toExternal

class PushRepository(
    private val api: PushApi,
) {

    suspend fun subscribe(
        endpoint: String,
        p256dh: String,
        auth: String,
    ): WebPushSubscription = api.subscribe(
        endpoint = endpoint,
        p256dh = p256dh,
        auth = auth,
    ).toExternal()

    suspend fun getSubscription(): WebPushSubscription = api.getSubscription().toExternal()
}