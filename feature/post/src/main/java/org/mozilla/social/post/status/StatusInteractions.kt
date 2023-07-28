package org.mozilla.social.post.status

import androidx.compose.ui.text.input.TextFieldValue

interface StatusInteractions {
    fun onStatusTextUpdated(textFieldValue: TextFieldValue) = Unit
    fun onAccountClicked(accountName: String) = Unit
    fun onHashtagClicked(hashtag: String) = Unit
}