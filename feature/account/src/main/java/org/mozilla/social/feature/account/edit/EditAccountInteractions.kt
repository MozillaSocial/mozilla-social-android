package org.mozilla.social.feature.account.edit

import org.mozilla.social.feature.account.AccountUiState

interface EditAccountInteractions {
    fun onDisplayNameTextChanged(text: String) = Unit
    fun onBioTextChanged(text: String) = Unit
}