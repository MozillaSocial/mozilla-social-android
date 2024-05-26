package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.URLProtocol
import io.ktor.http.parameters
import io.ktor.http.path
import social.firefly.core.network.mastodon.PushApi
import social.firefly.core.network.mastodon.model.responseBody.NetworkWebPushSubscription

class PushApiImpl(
    private val client: HttpClient,
) : PushApi {

    override suspend fun subscribe(
        endpoint: String,
        p256dh: String,
        auth: String,
        mention: Boolean,
        status: Boolean,
        reblog: Boolean,
        follow: Boolean,
        followRequest: Boolean,
        favourite: Boolean,
        poll: Boolean,
        update: Boolean,
        adminSignUp: Boolean,
        adminReport: Boolean,
        policy: String
    ): NetworkWebPushSubscription = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/push/subscription")
        }
        setBody(FormDataContent(
            parameters {
                append("subscription[endpoint]", endpoint)
                append("subscription[keys][p256dh]", p256dh)
                append("subscription[keys][auth]", auth)
                append("data[alerts][mention]", mention.toString())
                append("data[alerts][status]", status.toString())
                append("data[alerts][reblog]", reblog.toString())
                append("data[alerts][follow]", follow.toString())
                append("data[alerts][follow_request]", followRequest.toString())
                append("data[alerts][favourite]", favourite.toString())
                append("data[alerts][poll]", poll.toString())
                append("data[alerts][update]", update.toString())
                append("data[alerts][admin.sign_up]", adminSignUp.toString())
                append("data[alerts][admin.report]", adminReport.toString())
                append("data[policy]", policy)
            }
        ))
    }.body()

    override suspend fun getSubscription(): NetworkWebPushSubscription = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/push/subscription")
        }
    }.body()
}