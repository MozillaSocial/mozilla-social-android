package org.mozilla.social.feature.auth.chooseServer

import android.content.Context

internal interface ChooseServerInteractions {
    fun onServerTextChanged(text: String) = Unit
    fun onNextClicked(context: Context) = Unit
}