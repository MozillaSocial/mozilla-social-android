package org.mozilla.social.feature.auth.login

import android.content.Context

interface LoginInteractions {
    fun onScreenViewed() = Unit
    fun onSignInClicked(context: Context) = Unit
    fun onChooseServerClicked() = Unit
}