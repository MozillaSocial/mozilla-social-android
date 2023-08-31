package org.mozilla.social.core.network

import org.mozilla.social.core.network.model.NetworkAccount
import org.mozilla.social.core.network.model.NetworkStatus
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountApi {
    @GET("/api/v1/accounts/{id}")
    suspend fun getAccount(
        @Path("id") accountId: String
    ): NetworkAccount

    @GET("/api/v1/accounts/verify_credentials")
    suspend fun verifyAccount() : NetworkAccount

    @GET("/api/v1/accounts/{id}/followers")
    suspend fun getAccountFollowers(
        @Path("id") accountId: String
    ): List<NetworkAccount>

    @GET("/api/v1/accounts/{id}/following")
    suspend fun getAccountFollowing(
        @Path("id") accountId: String
    ): List<NetworkAccount>

    @GET("/api/v1/accounts/{id}/statuses")
    suspend fun getAccountStatuses(
        @Path("id") accountId: String
    ): List<NetworkStatus>

    @GET("/api/v1/bookmarks")
    suspend fun getAccountBookmarks(): List<NetworkStatus>

    @GET("/api/v1/favourites")
    suspend fun getAccountFavourites(): List<NetworkStatus>

    @POST("/api/v1/accounts/{accountId}/follow")
    suspend fun followAccount(
        @Path("accountId") accountId: String
    )

    @POST("/api/v1/accounts/{accountId}/unfollow")
    suspend fun unfollowAccount(
        @Path("accountId") accountId: String
    )
}