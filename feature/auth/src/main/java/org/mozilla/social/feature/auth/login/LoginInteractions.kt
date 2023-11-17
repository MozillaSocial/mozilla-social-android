package org.mozilla.social.feature.auth.login

interface LoginInteractions {
    fun onScreenViewed() = Unit

    fun onSignInClicked() = Unit

    fun onChooseServerClicked() = Unit
}
