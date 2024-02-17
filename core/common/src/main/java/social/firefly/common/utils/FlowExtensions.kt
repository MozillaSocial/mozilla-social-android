package social.firefly.common.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

fun <T> MutableStateFlow<T>.edit(block: T.() -> T) {
    update {
        it.block()
    }
}
