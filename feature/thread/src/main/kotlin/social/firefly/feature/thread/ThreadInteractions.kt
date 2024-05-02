package social.firefly.feature.thread

interface ThreadInteractions {
    fun onsScreenViewed() = Unit
    fun onThreadTypeSelected(threadType: ThreadType) = Unit
    fun onRetryClicked() = Unit
    fun onShowAllRepliesClicked(statusId: String) = Unit
    fun onPulledToRefresh() = Unit
}
