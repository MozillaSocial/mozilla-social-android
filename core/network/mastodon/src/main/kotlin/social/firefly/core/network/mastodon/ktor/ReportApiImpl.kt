package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import social.firefly.core.network.mastodon.ReportApi
import social.firefly.core.network.mastodon.model.request.NetworkReportCreate

class ReportApiImpl(
    private val client: HttpClient,
) : ReportApi {

    override suspend fun report(
        body: NetworkReportCreate
    ) {
        client.post {
            url {
                protocol = URLProtocol.HTTPS
                path("api/v1/reports")
            }
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}