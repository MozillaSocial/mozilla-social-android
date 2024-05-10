package social.firefly.feature.feed

data class FeedUiState(
    val timelineType: TimelineType = TimelineType.FOR_YOU,
    val scrollUpButtonVisible: Boolean = false
)