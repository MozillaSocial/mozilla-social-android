package social.firefly.core.ui.accountfollower

import social.firefly.core.ui.common.account.quickview.AccountQuickViewUiState

data class AccountFollowerUiState(
    val accountQuickViewUiState: AccountQuickViewUiState,
    val isFollowing: Boolean,
    val bioHtml: String,
    val followButtonVisible: Boolean,
)
