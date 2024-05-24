package social.firefly.core.network.mastodon.model.responseBody

data class NetworkPreferences(
    /**
     * Default visibility for new posts.
     *
     * Equivalent to [NetworkSource.defaultPrivacy].
     */
    val defaultVisibility: social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility,
    /**
     * Default sensitivity flag for new posts.
     */
    val defaultSensitivity: Boolean,
    /**
     * Default language for new posts.
     *
     * ISO 639-1 language two-letter code.
     */
    val defaultLanguage: String,
    /**
     * Whether media attachments should be automatically displayed or blurred/hidden.
     */
    val defaultMediaVisibility: social.firefly.core.network.mastodon.model.responseBody.NetworkMediaVisibility,
    /**
     * Whether statuses behind a content warning should be expanded by default.
     */
    val defaultExpandContentWarnings: Boolean,
)
