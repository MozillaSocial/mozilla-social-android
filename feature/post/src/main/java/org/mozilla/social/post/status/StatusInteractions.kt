package org.mozilla.social.post.status

import androidx.compose.ui.text.input.TextFieldValue

interface StatusInteractions {
    fun onStatusTextUpdated(textFieldValue: TextFieldValue) = Unit
}