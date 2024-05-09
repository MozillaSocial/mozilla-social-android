package social.firefly.feature.settings.account

interface AccountSettingsInteractions {
    fun onScreenViewed()
    fun onLogoutClicked(accountId: String, domain: String)
    fun onManageAccountClicked(domain: String)
    fun onAddAccountClicked()
    fun onSetAccountAsActiveClicked(accountId: String, domain: String)
    fun onLogoutOfAllAccountsClicked()
}

data object AccountSettingsInteractionsNoOp : AccountSettingsInteractions {
    override fun onScreenViewed() = Unit
    override fun onLogoutClicked(accountId: String, domain: String) = Unit
    override fun onManageAccountClicked(domain: String) = Unit
    override fun onAddAccountClicked() = Unit
    override fun onSetAccountAsActiveClicked(accountId: String, domain: String) = Unit
    override fun onLogoutOfAllAccountsClicked() = Unit
}