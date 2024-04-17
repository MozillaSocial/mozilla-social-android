package social.firefly.common.utils

inline fun <T> List<T>.indexOfFirst(startingAtIndex: Int, predicate: (T) -> Boolean): Int {
    for (index in startingAtIndex..<this.size) {
        if (predicate(this[index]))
            return index
    }
    return -1
}