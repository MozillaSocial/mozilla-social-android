package org.mozilla.social.core.network.model

enum class NetworkFilterContext {

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
    Thread
}
