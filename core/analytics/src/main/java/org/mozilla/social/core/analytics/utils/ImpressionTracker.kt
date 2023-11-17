package org.mozilla.social.core.analytics.utils

class ImpressionTracker<T>(
    private val onTrack: (key: T) -> Unit,
) {
    private val trackedValues: MutableList<T> = mutableListOf()

    fun track(key: T) {
        if (trackedValues.contains(key)) return
        trackedValues.add(key)
        onTrack(key)
    }
}
