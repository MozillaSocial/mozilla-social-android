package social.firefly.core.network.mastodon.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class NetworkNotification {
    abstract val id: String
    abstract val createdAt: Instant
    abstract val account: NetworkAccount

    /**
     * Someone mentioned you in their status
     */
    @Serializable
    @SerialName("mention")
    data class Mention(
        @SerialName("id")
        override val id: String,
        @SerialName("created_at")
        override val createdAt: Instant,
        @SerialName("account")
        override val account: NetworkAccount,
        @SerialName("status")
        val status: NetworkStatus,
    ) : NetworkNotification()

    /**
     * Someone you enabled notifications for has posted a status
     */
    @Serializable
    @SerialName("status")
    data class NewStatus(
        @SerialName("id")
        override val id: String,
        @SerialName("created_at")
        override val createdAt: Instant,
        @SerialName("account")
        override val account: NetworkAccount,
        @SerialName("status")
        val status: NetworkStatus,
    ) : NetworkNotification()

    /**
     * Someone boosted one of your statuses
     */
    @Serializable
    @SerialName("reblog")
    data class Repost(
        @SerialName("id")
        override val id: String,
        @SerialName("created_at")
        override val createdAt: Instant,
        @SerialName("account")
        override val account: NetworkAccount,
        @SerialName("status")
        val status: NetworkStatus,
    ) : NetworkNotification()

    /**
     * Someone followed you
     */
    @Serializable
    @SerialName("follow")
    data class Follow(
        @SerialName("id")
        override val id: String,
        @SerialName("created_at")
        override val createdAt: Instant,
        @SerialName("account")
        override val account: NetworkAccount,
    ) : NetworkNotification()

    /**
     * Someone requested to follow you
     */
    @Serializable
    @SerialName("follow_request")
    data class FollowRequest(
        @SerialName("id")
        override val id: String,
        @SerialName("created_at")
        override val createdAt: Instant,
        @SerialName("account")
        override val account: NetworkAccount,
    ) : NetworkNotification()

    /**
     * Someone favourited one of your statuses
     */
    @Serializable
    @SerialName("favourite")
    data class Favorite(
        @SerialName("id")
        override val id: String,
        @SerialName("created_at")
        override val createdAt: Instant,
        @SerialName("account")
        override val account: NetworkAccount,
        @SerialName("status")
        val status: NetworkStatus,
    ) : NetworkNotification()

    /**
     * A poll you have voted in or created has ended
     */
    @Serializable
    @SerialName("poll")
    data class PollEnded(
        @SerialName("id")
        override val id: String,
        @SerialName("created_at")
        override val createdAt: Instant,
        @SerialName("account")
        override val account: NetworkAccount,
        @SerialName("status")
        val status: NetworkStatus,
    ) : NetworkNotification()

    /**
     * A status you interacted with has been edited
     */
    @Serializable
    @SerialName("update")
    data class StatusUpdated(
        @SerialName("id")
        override val id: String,
        @SerialName("created_at")
        override val createdAt: Instant,
        @SerialName("account")
        override val account: NetworkAccount,
        @SerialName("status")
        val status: NetworkStatus,
    ) : NetworkNotification()

    @Serializable
    @SerialName("admin.sign_up")
    data class AdminSignUp(
        @SerialName("id")
        override val id: String,
        @SerialName("created_at")
        override val createdAt: Instant,
        @SerialName("account")
        override val account: NetworkAccount,
    ) : NetworkNotification()

    @Serializable
    @SerialName("admin.report")
    data class AdminReport(
        @SerialName("id")
        override val id: String,
        @SerialName("created_at")
        override val createdAt: Instant,
        @SerialName("account")
        override val account: NetworkAccount,
        @SerialName("report")
        val report: NetworkAdminReport,
    ) : NetworkNotification()

    @Serializable
    @SerialName("severed_relationships")
    data class SeveredRelationships(
        @SerialName("id")
        override val id: String,
        @SerialName("created_at")
        override val createdAt: Instant,
        @SerialName("account")
        override val account: NetworkAccount,
        @SerialName("relationship_severance_event")
        val severanceEvent: NetworkRelationshipSeveranceEvent,
    ) : NetworkNotification()
}
