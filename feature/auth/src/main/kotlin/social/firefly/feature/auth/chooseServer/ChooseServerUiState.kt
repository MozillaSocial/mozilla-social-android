package social.firefly.feature.auth.chooseServer

data class ChooseServerUiState(
    val serverText: String = "",
    val nextButtonEnabled: Boolean = false,
    val loginFailed: Boolean = false,
    val isLoading: Boolean = false,
    val suggestedServers: List<Server> = emptyList(),
)
