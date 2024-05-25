package social.firefly.core.network.mastodon

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import social.firefly.core.network.mastodon.model.responseBody.NetworkHashTag

interface TagsApi {

    @POST("api/v1/tags/{hashTag}/follow")
    suspend fun followHashTag(
        @Path("hashTag") hashTag: String,
    ): NetworkHashTag

    @POST("api/v1/tags/{hashTag}/unfollow")
    suspend fun unfollowHashTag(
        @Path("hashTag") hashTag: String,
    ): NetworkHashTag

    @GET("api/v1/tags/{hashTag}")
    suspend fun getHashTag(
        @Path("hashTag") hashTag: String,
    ): NetworkHashTag
}