package social.firefly.core.ui.chooseAccount

import kotlinx.coroutines.flow.Flow

data class ChooseAccountUiState(
    val accountId: Flow<String>,
    val userName: Flow<String>,
    val domain: Flow<String>,
    val avatarUrl: Flow<String>,
)