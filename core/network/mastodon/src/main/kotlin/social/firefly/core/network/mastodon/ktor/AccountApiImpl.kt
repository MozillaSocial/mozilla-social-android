package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.append
import okhttp3.MultipartBody
import okhttp3.RequestBody
import social.firefly.core.network.mastodon.AccountApi
import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkAccount
import social.firefly.core.network.mastodon.model.responseBody.NetworkRelationship
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus
import social.firefly.core.network.mastodon.utils.path
import social.firefly.core.network.mastodon.utils.toExternal

class AccountApiImpl(
    private val client: HttpClient,
) : AccountApi {

    override suspend fun getAccount(
        accountId: String
    ): Response<NetworkAccount> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/accounts/$accountId")
        }
    }.toExternal()

    override suspend fun getAccountFollowers(
        accountId: String,
        olderThanId: String?,
        newerThanId: String?,
        immediatelyNewerThanId: String?,
        limit: Int?
    ): Response<List<NetworkAccount>> = client.get {
        url { _ ->
            protocol = URLProtocol.HTTPS
            path("api/v1/accounts/$accountId/followers")
            parameters.apply {
                olderThanId?.let { append("max_id", it) }
                newerThanId?.let { append("since_id", it) }
                immediatelyNewerThanId?.let { append("min_id", it) }
                limit?.let { append("limit", it.toString()) }
            }
        }
    }.toExternal()

    override suspend fun getAccountFollowing(
        accountId: String,
        olderThanId: String?,
        newerThanId: String?,
        immediatelyNewerThanId: String?,
        limit: Int?
    ): Response<List<NetworkAccount>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountStatuses(
        accountId: String,
        olderThanId: String?,
        newerThanId: String?,
        immediatelyNewerThanId: String?,
        limit: Int?,
        onlyMedia: Boolean,
        excludeReplies: Boolean,
        excludeBoosts: Boolean
    ): Response<List<NetworkStatus>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountBookmarks(): List<NetworkStatus> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountFavourites(): List<NetworkStatus> {
        TODO("Not yet implemented")
    }

    override suspend fun followAccount(accountId: String): NetworkRelationship {
        TODO("Not yet implemented")
    }

    override suspend fun unfollowAccount(accountId: String): NetworkRelationship {
        TODO("Not yet implemented")
    }

    override suspend fun blockAccount(accountId: String): NetworkRelationship {
        TODO("Not yet implemented")
    }

    override suspend fun unblockAccount(accountId: String): NetworkRelationship {
        TODO("Not yet implemented")
    }

    override suspend fun muteAccount(
        accountId: String,
        duration: Int
    ): NetworkRelationship {
        TODO("Not yet implemented")
    }

    override suspend fun unmuteAccount(accountId: String): NetworkRelationship {
        TODO("Not yet implemented")
    }

    override suspend fun getRelationships(ids: Array<String>): List<NetworkRelationship> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAccount(
        displayName: RequestBody?,
        bio: RequestBody?,
        locked: RequestBody?,
        bot: RequestBody?,
        avatar: MultipartBody.Part?,
        header: MultipartBody.Part?,
        fieldLabel0: RequestBody?,
        fieldContent0: RequestBody?,
        fieldLabel1: RequestBody?,
        fieldContent1: RequestBody?,
        fieldLabel2: RequestBody?,
        fieldContent2: RequestBody?,
        fieldLabel3: RequestBody?,
        fieldContent3: RequestBody?
    ): NetworkAccount {
        TODO("Not yet implemented")
    }
}