package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import social.firefly.core.network.mastodon.MediaApi
import social.firefly.core.network.mastodon.model.request.NetworkMediaUpdate
import social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment
import java.io.File

class MediaApiImpl(
    private val client: HttpClient,
) : MediaApi {

    override suspend fun uploadMedia(
        file: File,
        description: String?
    ): NetworkAttachment = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v2/media")
        }
        setBody(MultiPartFormDataContent(
            formData {
                description?.let { append("description", description) }
                append("file", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "image/*")
                    append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                })
            }
        ))
    }.body()

    override suspend fun updateMedia(
        mediaId: String,
        requestBody: NetworkMediaUpdate
    ) {
        client.put {
            url {
                protocol = URLProtocol.HTTPS
                path("api/v1/media/$mediaId")
            }
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }
    }
}