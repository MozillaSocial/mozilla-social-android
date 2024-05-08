package social.firefly.feature.settings.account

interface AccountSettingsInteractions {
    fun onScreenViewed()
    fun onLogoutClicked()
    fun onManageAccountClicked(domain: String)
    fun onAddAccountClicked()
}

data object AccountSettingsInteractionsNoOp : AccountSettingsInteractions {
    override fun onScreenViewed() = Unit
    override fun onLogoutClicked() = Unit
    override fun onManageAccountClicked(domain: String) = Unit
    override fun onAddAccountClicked() = Unit
}