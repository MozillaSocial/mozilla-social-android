package org.mozilla.social.feature.account.edit

data class EditAccountUiState(
    val userName: String,
    val headerUrl: String,
    val avatarUrl: String,
    val handle: String,
    val displayName: String = "",
    val bio: String = "",
    val bioCharacterCount: Int = 0,
)

