package social.firefly.feature.settings

interface SettingsInteractions {
    fun onScreenViewed()
    fun onAboutClicked()
    fun onAccountClicked()
    fun onContentPreferencesClicked()
    fun onPrivacyClicked()
    fun onDeveloperOptionsClicked()
    fun onOpenSourceLicensesClicked()
    fun onAppearanceAndBehaviorClicked()
}

object SettingsInteractionsNoOp : SettingsInteractions {
    override fun onScreenViewed() = Unit
    override fun onAboutClicked() = Unit
    override fun onAccountClicked() = Unit
    override fun onContentPreferencesClicked() = Unit
    override fun onPrivacyClicked() = Unit
    override fun onDeveloperOptionsClicked() = Unit
    override fun onOpenSourceLicensesClicked() = Unit
    override fun onAppearanceAndBehaviorClicked() = Unit
}