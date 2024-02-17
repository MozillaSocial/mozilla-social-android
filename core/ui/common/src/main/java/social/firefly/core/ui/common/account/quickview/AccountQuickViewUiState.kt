package social.firefly.core.ui.common.account.quickview

data class AccountQuickViewUiState(
    val accountId: String,
    val displayName: String,
    val webFinger: String,
    val avatarUrl: String,
)
