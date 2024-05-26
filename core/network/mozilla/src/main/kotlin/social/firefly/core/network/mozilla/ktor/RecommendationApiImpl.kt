package social.firefly.core.network.mozilla.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import social.firefly.core.network.mozilla.RecommendationApi
import social.firefly.core.network.mozilla.model.NetworkRecommendations

class RecommendationApiImpl(
    private val client: HttpClient,
) : RecommendationApi {
    override suspend fun getRecommendations(
        locale: String,
        count: Int,
        imageSizes: String
    ): NetworkRecommendations = client.get { 
        url { 
            protocol = URLProtocol.HTTPS
            path("content-feed/Ff/v1/discover/")
            parameters.apply {
                append("locale", locale)
                append("count", count.toString())
                append("image_sizes[]", imageSizes)
            }
        }
    }.body()
}