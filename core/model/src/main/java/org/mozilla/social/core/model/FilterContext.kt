package org.mozilla.social.core.model

enum class FilterContext {
    /**
     * Home timeline and lists.
     */
    Home,

    /**
     * Notifications timeline.
     */
    Notifications,

    /**
     * Public timelines.
     */
    Public,

    /**
     * Expanded thread of a detailed status.
     */
    Thread,
}
