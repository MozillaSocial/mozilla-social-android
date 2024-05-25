package social.firefly.core.network.mastodon

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment
import social.firefly.core.network.mastodon.model.request.NetworkMediaUpdate

interface MediaApi {
    @Multipart
    @POST("/api/v2/media")
    suspend fun uploadMedia(
        @Part file: MultipartBody.Part,
        @Part("description") description: String? = null,
    ): NetworkAttachment

    @PUT("api/v1/media/{mediaId}")
    suspend fun updateMedia(
        @Path("mediaId") mediaId: String,
        @Body requestBody: NetworkMediaUpdate,
    )
}
