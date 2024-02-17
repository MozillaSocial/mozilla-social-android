package social.firefly.feature.notifications

data class NotificationsUiState(
    val selectedTab: NotificationsTab = NotificationsTab.ALL,
    val requestsTabIsVisible: Boolean = false,
)