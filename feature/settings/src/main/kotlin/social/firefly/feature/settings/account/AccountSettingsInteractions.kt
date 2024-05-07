package social.firefly.feature.settings.account

interface AccountSettingsInteractions {
    fun onScreenViewed()
    fun onLogoutClicked()
    fun onManageAccountClicked(domain: String)
}

data object AccountSettingsInteractionsNoOp : AccountSettingsInteractions {
    override fun onScreenViewed() = Unit
    override fun onLogoutClicked() = Unit
    override fun onManageAccountClicked(domain: String) = Unit
}