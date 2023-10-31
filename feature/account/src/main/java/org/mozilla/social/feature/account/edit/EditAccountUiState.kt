package org.mozilla.social.feature.account.edit

data class EditAccountUiState(
    val topBarTitle: String,
    val headerUrl: String,
    val avatarUrl: String,
    val handle: String,
    val displayName: String = "",
    val bio: String = "",
    val bioCharacterCount: Int = 0,
    val lockChecked: Boolean,
    val botChecked: Boolean,
)

