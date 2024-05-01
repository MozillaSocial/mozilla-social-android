package social.firefly.core.model

enum class DirectoryOrder(val value: String) {
    /**
     * Sort by most recently posted statuses.
     */
    Active("active"),

    /**
     * Sort by most recently created accounts.
     */
    New("new"),
}
