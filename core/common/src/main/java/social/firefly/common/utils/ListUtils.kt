package social.firefly.common.utils

inline fun <T> List<T>.indexOfFirst(predicate: (T) -> Boolean, startingAtIndex: Int): Int {
    for (index in startingAtIndex..this.size) {
        if (predicate(this[index]))
            return index
    }
    return -1
}