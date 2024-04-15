package social.firefly.feature.auth.login

interface LoginInteractions {
    fun onScreenViewed() = Unit
    fun onSignInClicked() = Unit
    fun onChooseServerClicked() = Unit
    fun onSignInToStagingClicked() = Unit
}
