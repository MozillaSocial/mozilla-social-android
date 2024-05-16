package social.firefly.feature.settings.contentpreferences.blockedDomains

interface BlockedDomainInteractions {
    fun onUnblockClicked(domain: String)
    fun onBlockClicked(domain: String)
}