package org.mozilla.social.feature.notifications

data class NotificationsUiState(
    val selectedTab: NotificationsTab = NotificationsTab.ALL,
    val requestsTabIsVisible: Boolean = false,
)