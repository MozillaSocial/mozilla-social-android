package social.firefly.core.ui.accountfollower

import social.firefly.core.ui.common.account.quickview.AccountQuickViewUiState
import social.firefly.core.ui.common.following.FollowStatus

data class AccountFollowerUiState(
    val accountQuickViewUiState: AccountQuickViewUiState,
    val followStatus: FollowStatus,
    val bioHtml: String,
    val followButtonVisible: Boolean,
)
