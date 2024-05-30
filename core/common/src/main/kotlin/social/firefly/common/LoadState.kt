package social.firefly.common

sealed class LoadState {
    data object LOADING : LoadState()

    data object LOADED : LoadState()

    data object ERROR : LoadState()
}
