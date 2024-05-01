package social.firefly.core.model

import kotlinx.datetime.Instant

sealed class Notification {
    abstract val id: Int
    abstract val createdAt: Instant
    abstract val account: Account

    /**
     * Someone mentioned you in their status
     */
    data class Mention(
        override val id: Int,
        override val createdAt: Instant,
        override val account: Account,
        val status: Status,
    ) : Notification() {
        companion object {
            // used for api requests
            const val VALUE = "mention"
        }
    }

    /**
     * Someone you enabled notifications for has posted a status
     */
    data class NewStatus(
        override val id: Int,
        override val createdAt: Instant,
        override val account: Account,
        val status: Status,
    ) : Notification()

    /**
     * Someone boosted one of your statuses
     */
    data class Repost(
        override val id: Int,
        override val createdAt: Instant,
        override val account: Account,
        val status: Status,
    ) : Notification()

    /**
     * Someone followed you
     */
    data class Follow(
        override val id: Int,
        override val createdAt: Instant,
        override val account: Account,
    ) : Notification() {
        companion object {
            // used for api requests
            const val VALUE = "follow"
        }
    }

    /**
     * Someone requested to follow you
     */
    data class FollowRequest(
        override val id: Int,
        override val createdAt: Instant,
        override val account: Account,
    ) : Notification() {
        companion object {
            // used for api requests
            const val VALUE = "follow_request"
        }
    }

    /**
     * Someone favourited one of your statuses
     */
    data class Favorite(
        override val id: Int,
        override val createdAt: Instant,
        override val account: Account,
        val status: Status,
    ) : Notification()

    /**
     * A poll you have voted in or created has ended
     */
    data class PollEnded(
        override val id: Int,
        override val createdAt: Instant,
        override val account: Account,
        val status: Status,
    ) : Notification()

    /**
     * A status you interacted with has been edited
     */
    data class StatusUpdated(
        override val id: Int,
        override val createdAt: Instant,
        override val account: Account,
        val status: Status,
    ) : Notification()

    data class AdminSignUp(
        override val id: Int,
        override val createdAt: Instant,
        override val account: Account,
    ) : Notification()

    data class AdminReport(
        override val id: Int,
        override val createdAt: Instant,
        override val account: Account,
        val report: social.firefly.core.model.AdminReport,
    ) : Notification()

    data class SeveredRelationships(
        override val id: Int,
        override val createdAt: Instant,
        override val account: Account,
        val severanceEvent: RelationshipSeveranceEvent,
    ) : Notification()
}
