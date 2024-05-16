package social.firefly.feature.settings.contentpreferences

interface ContentPreferencesSettingsInteractions {
    fun onScreenViewed()
    fun onMutedUsersClicked()
    fun onBlockedUsersClicked()
    fun onBlockedDomainsClicked()
}