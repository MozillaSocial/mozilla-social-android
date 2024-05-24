package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkAccount
import social.firefly.core.network.mastodon.model.responseBody.NetworkRelationship
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus
import java.io.File

interface AccountApi {
    suspend fun getAccount(
        accountId: String,
    ): NetworkAccount

    suspend fun getAccountFollowers(
        accountId: String,
        olderThanId: String? = null,
        newerThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        limit: Int? = null,
    ): Response<List<NetworkAccount>>

    suspend fun getAccountFollowing(
        accountId: String,
        olderThanId: String? = null,
        newerThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        limit: Int? = null,
    ): Response<List<NetworkAccount>>

    suspend fun getAccountStatuses(
        accountId: String,
        olderThanId: String? = null,
        newerThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        limit: Int? = null,
        onlyMedia: Boolean = false,
        excludeReplies: Boolean = false,
        excludeBoosts: Boolean = false,
    ): Response<List<NetworkStatus>>

    suspend fun getAccountBookmarks(): List<NetworkStatus>

    suspend fun getAccountFavourites(): List<NetworkStatus>

    suspend fun followAccount(
        accountId: String,
    ): NetworkRelationship

    suspend fun unfollowAccount(
        accountId: String,
    ): NetworkRelationship

    suspend fun blockAccount(
        accountId: String,
    ): NetworkRelationship

    suspend fun unblockAccount(
        accountId: String,
    ): NetworkRelationship

    suspend fun muteAccount(
        accountId: String,
        duration: Int = 0,
    ): NetworkRelationship

    suspend fun unmuteAccount(
        accountId: String,
    ): NetworkRelationship

    suspend fun getRelationships(
        ids: List<String>,
    ): List<NetworkRelationship>

    suspend fun updateAccount(
        displayName: String? = null,
        bio: String? = null,
        locked: Boolean? = null,
        bot: Boolean? = null,
        avatar: File? = null,
        header: File? = null,
        fields: List<Pair<String, String>>? = null,
    ): NetworkAccount
}
