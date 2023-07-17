package org.mozilla.social.feature.auth

 sealed class UiState {
    object Default : UiState()
    object SignedIn : UiState()
}