package social.firefly.core.network.mastodon.model.responseBody

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
    Thread,
}
