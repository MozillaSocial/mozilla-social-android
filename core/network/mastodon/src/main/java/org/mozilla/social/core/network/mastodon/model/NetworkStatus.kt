package org.mozilla.social.core.network.mastodon.model


import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mozilla.social.core.network.mastodon.model.paging.NetworkPageable

/**
 * Represents a status posted by an account.
 */
@Serializable
data class NetworkStatus(

    @SerialName("id")
    val statusId: String,

    /**
     * URI of the status used for federation.
     */
    @SerialName("uri")
    val uri: String,

    /**
     * The time when this status was created.
     */
    @SerialName("created_at")
    val createdAt: Instant,

    /**
     * The account that authored this status.
     */
    @SerialName("account")
    val account: NetworkAccount,

    /**
     * HTML-encoded status content.
     */
    @SerialName("content")
    val content: String,

    /**
     * Visibility of this status.
     */
    @SerialName("visibility")
    val visibility: NetworkStatusVisibility,

    /**
     * Is this status marked as sensitive content?
     */
    @SerialName("sensitive")
    val isSensitive: Boolean,

    /**
     * Subject or summary line, below which status content is collapsed until expanded.
     */
    @SerialName("spoiler_text")
    val contentWarningText: String,

    /**
     * Media that is attached to this status.
     */
    @SerialName("media_attachments")
    val mediaAttachments: List<NetworkAttachment>,

    /**
     * Mentions of users within the status content.
     */
    @SerialName("mentions")
    val mentions: List<NetworkMention>,

    /**
     * Hashtags used within the status content.
     */
    @SerialName("tags")
    val hashTags: List<NetworkHashTag>,

    /**
     * Custom emoji to be used when rendering status content.
     */
    @SerialName("emojis")
    val emojis: List<NetworkEmoji>,

    /**
     * How many boosts this status has received.
     */
    @SerialName("reblogs_count")
    val boostsCount: Long,

    /**
     * How many favourites this status has received.
     */
    @SerialName("favourites_count")
    val favouritesCount: Long,

    /**
     * How many replies this status has received.
     */
    @SerialName("replies_count")
    val repliesCount: Long,

    /**
     * The application used to post this status.
     */
    @SerialName("application")
    val application: NetworkApplication? = null,

    /**
     * A link to the status's HTML representation.
     */
    @SerialName("url")
    val url: String? = null,

    /**
     * ID of the status being replied to.
     */
    @SerialName("in_reply_to_id")
    val inReplyToId: String? = null,

    /**
     * ID of the account being replied to.
     */
    @SerialName("in_reply_to_account_id")
    val inReplyToAccountId: String? = null,

    /**
     * The status being boosted.
     */
    @SerialName("reblog")
    val boostedStatus: NetworkStatus? = null,

    /**
     * The poll attached to the status.
     */
    @SerialName("poll")
    val poll: NetworkPoll? = null,

    /**
     * Preview card for links included within status content.
     */
    @SerialName("card")
    val card: NetworkCard? = null,

    /**
     * Primary language of this status.
     *
     * ISO 639-1 language two-letter code.
     */
    @SerialName("language")
    val language: String? = null,

    /**
     * Plain-text source of a status.
     *
     * Returned instead of [content] when the status is deleted,
     * so the user may redraft from the source text without the
     * client having to reverse-engineer the original text from
     * the HTML content.
     */
    @SerialName("text")
    val plainText: String? = null,

    /**
     * Whether the current account has favourited this status.
     */
    @SerialName("favourited")
    val isFavourited: Boolean? = null,

    /**
     * Whether the current account has boosted this status.
     */
    @SerialName("reblogged")
    val isBoosted: Boolean? = null,

    /**
     * Whether the current account has muted notifications for this conversation.
     */
    @SerialName("muted")
    val isMuted: Boolean? = null,

    /**
     * Whether the current account has bookmarked this status.
     */
    @SerialName("bookmarked")
    val isBookmarked: Boolean? = null,

    /**
     * Whether the current account has pinned this status.
     *
     * Only appears if the status is pinnable.
     */
    @SerialName("pinned")
    val isPinned: Boolean? = null,
) : NetworkPageable {

    override val id: String
        get() = statusId
}
