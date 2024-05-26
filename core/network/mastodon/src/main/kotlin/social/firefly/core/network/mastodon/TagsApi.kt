package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.responseBody.NetworkHashTag

interface TagsApi {

    suspend fun followHashTag(
        hashTag: String,
    ): NetworkHashTag

    suspend fun unfollowHashTag(
        hashTag: String,
    ): NetworkHashTag

    suspend fun getHashTag(
        hashTag: String,
    ): NetworkHashTag
}