package org.mozilla.social.core.ui.account.quickview

data class AccountQuickViewUiState(
    val accountId: String,
    val displayName: String,
    val webFinger: String,
    val avatarUrl: String,
)