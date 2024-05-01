package social.firefly.feature.discover

data class DiscoverUiState(
    val selectedTab: DiscoverTab,
    val tabs: List<DiscoverTab>,
)