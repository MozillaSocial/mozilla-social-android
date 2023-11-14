package org.mozilla.social.feature.auth.chooseServer

internal interface ChooseServerInteractions {
    fun onServerTextChanged(text: String) = Unit
    fun onNextClicked() = Unit
}