package org.mozilla.social.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

fun <R> CoroutineScope.launchSupervisor(block: suspend CoroutineScope.() -> R) =
    launch {
        supervisorScope { block.invoke(this) }
    }
