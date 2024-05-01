package social.firefly.core.network.mastodon.model

enum class NetworkDirectoryOrder(val value: String) {
    /**
     * Sort by most recently posted statuses.
     */
    Active("active"),

    /**
     * Sort by most recently created accounts.
     */
    New("new"),
}
