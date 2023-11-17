package org.mozilla.social.common

sealed class LoadState {
    object LOADING : LoadState()

    object LOADED : LoadState()

    object ERROR : LoadState()
}
