package social.firefly.core.model

data class HashTag(
    /**
     * The value of the hashtag after the # sign.
     */
    val name: String,
    /**
     * URL to the hashtag on the instance.
     */
    val url: String,
    /**
     * If the user is following the hashtag
     */
    val following: Boolean,
    /**
     * Hashtag usage statistics for given days.
     */
    val history: List<History>? = null,
)