package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.parameters
import io.ktor.http.path
import social.firefly.core.model.exceptions.AccountNotFoundException
import social.firefly.core.network.mastodon.AccountApi
import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkAccount
import social.firefly.core.network.mastodon.model.responseBody.NetworkRelationship
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus
import social.firefly.core.network.mastodon.utils.toResponse
import java.io.File

class AccountApiImpl(
    private val client: HttpClient,
) : AccountApi {

    @Suppress("MagicNumber")
    override suspend fun getAccount(
        accountId: String
    ): NetworkAccount {
        try {
            return client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    path("api/v1/accounts/$accountId")
                }
            }.body()
        } catch (e: ClientRequestException) {
            if (e.response.status.value == 404) {
                throw AccountNotFoundException(e)
            } else {
                throw e
            }
        }
    }

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
    }.toResponse()

    override suspend fun getAccountFollowing(
        accountId: String,
        olderThanId: String?,
        newerThanId: String?,
        immediatelyNewerThanId: String?,
        limit: Int?
    ): Response<List<NetworkAccount>> = client.get {
        url { _ ->
            protocol = URLProtocol.HTTPS
            path("api/v1/accounts/$accountId/following")
            parameters.apply {
                olderThanId?.let { append("max_id", it) }
                newerThanId?.let { append("since_id", it) }
                immediatelyNewerThanId?.let { append("min_id", it) }
                limit?.let { append("limit", it.toString()) }
            }
        }
    }.toResponse()

    override suspend fun getAccountStatuses(
        accountId: String,
        olderThanId: String?,
        newerThanId: String?,
        immediatelyNewerThanId: String?,
        limit: Int?,
        onlyMedia: Boolean,
        excludeReplies: Boolean,
        excludeBoosts: Boolean
    ): Response<List<NetworkStatus>> = client.get {
        url { _ ->
            protocol = URLProtocol.HTTPS
            path("api/v1/accounts/$accountId/statuses")
            parameters.apply {
                olderThanId?.let { append("max_id", it) }
                newerThanId?.let { append("since_id", it) }
                immediatelyNewerThanId?.let { append("min_id", it) }
                limit?.let { append("limit", it.toString()) }
                append("only_media", onlyMedia.toString())
                append("exclude_replies", excludeReplies.toString())
                append("exclude_reblogs", excludeBoosts.toString())
            }
        }
    }.toResponse()

    override suspend fun getAccountBookmarks(): List<NetworkStatus> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/bookmarks")
        }
    }.body()

    override suspend fun getAccountFavourites(): List<NetworkStatus> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/favourites")
        }
    }.body()

    override suspend fun followAccount(accountId: String): NetworkRelationship = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/accounts/$accountId/follow")
        }
    }.body()

    override suspend fun unfollowAccount(accountId: String): NetworkRelationship = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/accounts/$accountId/unfollow")
        }
    }.body()

    override suspend fun blockAccount(accountId: String): NetworkRelationship = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/accounts/$accountId/block")
        }
    }.body()

    override suspend fun unblockAccount(accountId: String): NetworkRelationship = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/accounts/$accountId/unblock")
        }
    }.body()

    override suspend fun muteAccount(
        accountId: String,
        duration: Int
    ): NetworkRelationship = client.submitForm(
        formParameters = parameters {
            append("duration", duration.toString())
        }
    ) {
        method = HttpMethod.Post
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/accounts/$accountId/mute")
        }
    }.body()

    override suspend fun unmuteAccount(accountId: String): NetworkRelationship = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/accounts/$accountId/unmute")
        }
    }.body()

    override suspend fun getRelationships(ids: List<String>): List<NetworkRelationship> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/accounts/relationships")
            parameters.appendAll("id[]", ids)
        }
    }.body()

    override suspend fun updateAccount(
        displayName: String?,
        bio: String?,
        locked: Boolean?,
        bot: Boolean?,
        avatar: File?,
        header: File?,
        fields: List<Pair<String, String>>?,
    ): NetworkAccount = client.patch {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/accounts/update_credentials")
        }
        setBody(MultiPartFormDataContent(
            formData {
                displayName?.let { append("display_name", it) }
                bio?.let { append("note", it) }
                locked?.let { append("locked", it) }
                bot?.let { append("bot", it) }
                avatar?.let {
                    append("avatar", it.readBytes())
                }
                header?.let {
                    append("header", it.readBytes())
                }
                fields?.forEachIndexed { index, pair ->
                    append("fields_attributes[$index][name]", pair.first)
                    append("fields_attributes[$index][value]", pair.second)
                }
            }
        ))
    }.body()
}