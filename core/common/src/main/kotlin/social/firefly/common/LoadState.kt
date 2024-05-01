package social.firefly.common

sealed class LoadState {
    object LOADING : social.firefly.common.LoadState()

    object LOADED : social.firefly.common.LoadState()

    object ERROR : social.firefly.common.LoadState()
}
