package social.firefly.feature.settings.contentpreferences.blockedDomains

data class BlockedDomainState(
    val domain: String,
    val isBlocked: Boolean,
)