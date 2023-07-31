package org.mozilla.social.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a custom emoji.
 */
@Serializable
data class Emoji(

    /**
     * The name of the custom emoji.
     */
    @SerialName("shortcode")
    val shortCode: String,

    /**
     * URL to the custom emoji.
     */
    @SerialName("url")
    val url: String,

    /**
     * URL to a static version of the custom emoji.
     */
    @SerialName("static_url")
    val staticUrl: String,

    /**
     * Whether this Emoji should be visible when composing
     * a new post, or unlisted.
     */
    @SerialName("visible_in_picker")
    val isVisibleInPicker: Boolean,

    /**
     * Used for sorting custom emoji in the picker.
     */
    @SerialName("category")
    val category: String? = null
)
