package org.mozilla.social.core.database.model

import kotlinx.serialization.Serializable

/**
 * Represents a custom emoji.
 */
@Serializable
data class DatabaseEmoji(
    /**
     * The name of the custom emoji.
     */
    val shortCode: String,
    /**
     * URL to the custom emoji.
     */
    val url: String,
    /**
     * URL to a static version of the custom emoji.
     */
    val staticUrl: String,
    /**
     * Whether this Emoji should be visible when composing
     * a new post, or unlisted.
     */
    val isVisibleInPicker: Boolean,
    /**
     * Used for sorting custom emoji in the picker.
     */
    val category: String? = null,
)
