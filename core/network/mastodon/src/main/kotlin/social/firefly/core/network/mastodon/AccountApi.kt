package social.firefly.core.network.mastodon

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkAccount
import social.firefly.core.network.mastodon.model.responseBody.NetworkRelationship
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus
import java.io.File

interface AccountApi {
    suspend fun getAccount(
        accountId: String,
    ): Response<NetworkAccount>

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

    @GET("/api/v1/accounts/relationships")
    suspend fun getRelationships(
        @Query("id[]") ids: List<String>,
    ): List<NetworkRelationship>

    @Multipart
    @PATCH("/api/v1/accounts/update_credentials")
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
