package org.mozilla.social.feature.auth

import android.content.Context

interface AuthInteractions {
    fun onScreenViewed() = Unit
    fun onSignInClicked(context: Context) = Unit
    fun onChooseServerClicked() = Unit
}