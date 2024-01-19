package org.mozilla.social.core.ui.accountfollower

import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewUiState

data class AccountFollowerUiState(
    val accountQuickViewUiState: AccountQuickViewUiState,
    val isFollowing: Boolean,
    val bioHtml: String,
    val followButtonVisible: Boolean,
)
