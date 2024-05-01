package social.firefly.core.network.mastodon.model

import kotlinx.datetime.Instant

/**
 * Represents a proof from an external identity provider.
 */
data class NetworkIdentityProof(
    /**
     * The name of the identity provider.
     */
    val provider: String,
    /**
     * The account owner's username on the identity provider's service.
     */
    val providerUsername: String,
    /**
     * When the identity proof was last updated.
     */
    val updatedAt: Instant,
    /**
     * URL to a statement of identity proof, hosted by the identity provider.
     */
    val proofUrl: String,
    /**
     * The account owner's profile URL on the identity provider.
     */
    val profileUrl: String,
)
