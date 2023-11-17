package org.mozilla.social.core.network.mastodon

import okhttp3.MultipartBody
import org.mozilla.social.core.network.mastodon.model.NetworkAttachment
import org.mozilla.social.core.network.mastodon.model.request.NetworkMediaUpdate
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

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
