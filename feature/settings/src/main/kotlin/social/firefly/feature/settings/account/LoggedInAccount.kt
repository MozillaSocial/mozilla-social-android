package social.firefly.feature.settings.account

import kotlinx.coroutines.flow.Flow

data class LoggedInAccount(
    val accountId: Flow<String>,
    val userName: Flow<String>,
    val domain: Flow<String>,
    val avatarUrl: Flow<String>,
)
