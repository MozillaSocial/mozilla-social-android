package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.responseBody.NetworkContext
import social.firefly.core.network.mastodon.model.responseBody.NetworkPoll
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus
import social.firefly.core.network.mastodon.model.request.NetworkPollVote
import social.firefly.core.network.mastodon.model.request.NetworkStatusCreate

interface StatusApi {

    suspend fun getStatus(
        statusId: String,
    ): NetworkStatus

    suspend fun postStatus(
        status: NetworkStatusCreate,
    ): NetworkStatus

    suspend fun editStatus(
        statusId: String,
        status: NetworkStatusCreate,
    ): NetworkStatus

    suspend fun voteOnPoll(
        pollId: String,
        body: NetworkPollVote,
    ): NetworkPoll

    suspend fun boostStatus(
        statusId: String,
    ): NetworkStatus

    suspend fun unBoostStatus(
        statusId: String,
    ): NetworkStatus

    suspend fun favoriteStatus(
        statusId: String,
    ): NetworkStatus

    suspend fun unFavoriteStatus(
        statusId: String,
    ): NetworkStatus

    suspend fun getStatusContext(
        statusId: String,
    ): NetworkContext

    suspend fun deleteStatus(
        statusId: String,
    )

    suspend fun bookmarkStatus(
        statusId: String,
    ): NetworkStatus

    suspend fun unbookmarkStatus(
        statusId: String,
    ): NetworkStatus
}
