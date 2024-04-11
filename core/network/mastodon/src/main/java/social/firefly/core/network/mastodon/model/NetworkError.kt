package social.firefly.core.network.mastodon.model

/**
 * Represents an API error.
 */
data class NetworkError(
    /**
     * The error message.
     */
    val error: String,
    /**
     * A longer description of the error.
     */
    val errorDescription: String? = null,
)
