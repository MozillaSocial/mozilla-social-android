package org.mozilla.social.feature.auth

internal sealed class UiState {
    object Default : UiState()
    object SignedIn : UiState()
}