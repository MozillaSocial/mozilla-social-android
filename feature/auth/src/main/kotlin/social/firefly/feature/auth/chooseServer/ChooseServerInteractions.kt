package social.firefly.feature.auth.chooseServer

internal interface ChooseServerInteractions {
    fun onServerTextChanged(text: String) = Unit
    fun onNextClicked() = Unit
    fun onScreenViewed() = Unit
    fun onUserCodeReceived(code: String) = Unit
    fun onServerListLoaded(servers: List<String>) = Unit
    fun onServerSuggestionClicked(server: String) = Unit
}
