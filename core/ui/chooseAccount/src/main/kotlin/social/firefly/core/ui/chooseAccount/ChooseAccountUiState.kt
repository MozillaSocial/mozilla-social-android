package social.firefly.core.ui.chooseAccount

data class ChooseAccountUiState(
    val accountId: String,
    val userName: String,
    val domain: String,
    val avatarUrl: String,
)