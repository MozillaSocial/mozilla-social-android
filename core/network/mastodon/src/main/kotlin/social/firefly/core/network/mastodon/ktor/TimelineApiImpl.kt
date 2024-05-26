package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.append
import io.ktor.http.path
import social.firefly.core.network.mastodon.TimelineApi
import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus
import social.firefly.core.network.mastodon.utils.toResponse

class TimelineApiImpl(
    private val client: HttpClient,
) : TimelineApi {
    override suspend fun getHomeTimeline(
        maxId: String?,
        sinceId: String?,
        minId: String?,
        limit: Int?
    ): Response<List<NetworkStatus>> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/timelines/home")
            parameters.apply {
                maxId?.let { append("max_id", it) }
                sinceId?.let { append("since_id", it) }
                minId?.let { append("min_id", it) }
                limit?.let { append("limit", it.toString()) }
            }
        }
    }.toResponse()

    override suspend fun getPublicTimeline(
        localOnly: Boolean?,
        federatedOnly: Boolean?,
        mediaOnly: Boolean?,
        maxId: String?,
        sinceId: String?,
        minId: String?,
        limit: Int?
    ): Response<List<NetworkStatus>> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/timelines/public")
            parameters.apply {
                maxId?.let { append("max_id", it) }
                sinceId?.let { append("since_id", it) }
                minId?.let { append("min_id", it) }
                limit?.let { append("limit", it.toString()) }
                localOnly?.let { append("local", localOnly.toString()) }
                federatedOnly?.let { append("remote", federatedOnly.toString()) }
                mediaOnly?.let { append("only_media", mediaOnly.toString()) }
            }
        }
    }.toResponse()

    override suspend fun getHashTagTimeline(
        hashTag: String,
        maxId: String?,
        sinceId: String?,
        minId: String?,
        limit: Int?
    ): Response<List<NetworkStatus>> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/timelines/tag/$hashTag")
            parameters.apply {
                maxId?.let { append("max_id", it) }
                sinceId?.let { append("since_id", it) }
                minId?.let { append("min_id", it) }
                limit?.let { append("limit", it.toString()) }
            }
        }
    }.toResponse()
}