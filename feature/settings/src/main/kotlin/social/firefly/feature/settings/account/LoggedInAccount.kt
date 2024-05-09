package social.firefly.feature.settings.account

data class LoggedInAccount(
    val accountId: String = "",
    val userName: String = "",
    val domain: String = "",
    val avatarUrl: String = "",
)
