package org.mozilla.social.core.network.mastodon

import org.mozilla.social.core.network.mastodon.model.NetworkHashTag
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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