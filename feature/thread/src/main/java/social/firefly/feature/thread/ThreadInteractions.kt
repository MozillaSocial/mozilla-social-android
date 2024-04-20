package social.firefly.feature.thread

interface ThreadInteractions {
    fun onsScreenViewed() = Unit
    fun onThreadTypeSelected(threadType: ThreadType) = Unit
}
