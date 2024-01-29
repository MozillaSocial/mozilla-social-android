package org.mozilla.social.post.status

import androidx.compose.ui.text.input.TextFieldValue

data class StatusUiState(
    val statusText: TextFieldValue = TextFieldValue(""),
    val accountList: List<Account>? = null,
    val hashtagList: List<String>? = null,
    val contentWarningText: String? = null,
    val inReplyToAccountName: String? = null,
    val editStatusId: String? = null,
)