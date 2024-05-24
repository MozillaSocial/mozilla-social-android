package social.firefly.core.network.mastodon.model.responseBody

enum class NetworkSearchType(val value: String) {
    /**
     * Search for accounts.
     */
    Accounts("accounts"),

    /**
     * Search for hashtags.
     */
    Hashtags("hashtags"),

    /**
     * Search for statuses.
     */
    Statuses("statuses"),
}
