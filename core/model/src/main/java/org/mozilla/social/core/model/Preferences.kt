package org.mozilla.social.core.model

data class Preferences(
    /**
     * Default visibility for new posts.
     *
     * Equivalent to [Source.defaultPrivacy].
     */
    val defaultVisibility: StatusVisibility,
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
    val defaultMediaVisibility: MediaVisibility,
    /**
     * Whether statuses behind a content warning should be expanded by default.
     */
    val defaultExpandContentWarnings: Boolean,
)
