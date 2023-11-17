package org.mozilla.social.core.network.mastodon.model

/**
 * Preferred media content visibility, i.e. whether media attachments
 * should be automatically displayed or blurred/hidden.
 */
enum class NetworkMediaVisibility {
    /**
     * Hide media marked as sensitive.
     */
    Default,

    /**
     * Always show all media by default, regardless of sensitivity.
     */
    ShowAll,

    /**
     * Always hide all media by default, regardless of sensitivity.
     */
    HideAll,
}
