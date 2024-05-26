package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import social.firefly.core.network.mastodon.InstanceApi
import social.firefly.core.network.mastodon.model.responseBody.NetworkExtendedDescription
import social.firefly.core.network.mastodon.model.responseBody.NetworkInstance
import social.firefly.core.network.mastodon.model.responseBody.NetworkInstanceRule

class InstanceApiImpl(
    private val client: HttpClient,
) : InstanceApi {
    
    override suspend fun getRules(): List<NetworkInstanceRule> = client.get { 
        url { 
            protocol = URLProtocol.HTTPS
            path("api/v1/instance/rules")
        }
    }.body()

    override suspend fun getInstance(): NetworkInstance = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v2/instance")
        }
    }.body()

    override suspend fun getExtendedDescription(): NetworkExtendedDescription = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/instance/extended_description")
        }
    }.body()
}