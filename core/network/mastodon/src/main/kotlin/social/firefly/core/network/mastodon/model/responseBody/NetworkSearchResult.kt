package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the results of a search.
 */
@Serializable
data class NetworkSearchResult(
    /**
     * Accounts which matched the query.
     */
    @SerialName("accounts")
    val accounts: List<NetworkAccount>,
    /**
     * Statuses which matched the query.
     */
    @SerialName("statuses")
    val statuses: List<NetworkStatus>,
    /**
     * Hashtags which matched the query.
     */
    @SerialName("hashtags")
    val hashtags: List<NetworkHashTag>,
)
