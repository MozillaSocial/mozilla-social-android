package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import social.firefly.core.network.mastodon.NotificationsApi
import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkNotification
import social.firefly.core.network.mastodon.utils.toResponse

class NotificationsApiImpl(
    private val client: HttpClient,
) : NotificationsApi {

    override suspend fun getNotifications(
        maxId: String?,
        sinceId: String?,
        minId: String?,
        limit: Int?,
        types: List<String>?,
        excludeTypes: List<String>?,
        accountId: String?
    ): Response<List<NetworkNotification>> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/notifications")
            parameters.apply {
                maxId?.let { append("max_id", it) }
                sinceId?.let { append("since_id", it) }
                minId?.let { append("min_id", it) }
                limit?.let { append("limit", it.toString()) }
                types?.let { appendAll("types[]", types) }
                excludeTypes?.let { appendAll("exclude_types[]", excludeTypes) }
                accountId?.let { append("account_id", accountId) }
            }
        }
    }.toResponse()
}