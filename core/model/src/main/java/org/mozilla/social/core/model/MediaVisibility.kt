package org.mozilla.social.core.model

/**
 * Preferred media content visibility, i.e. whether media attachments
 * should be automatically displayed or blurred/hidden.
 */
enum class MediaVisibility {
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
