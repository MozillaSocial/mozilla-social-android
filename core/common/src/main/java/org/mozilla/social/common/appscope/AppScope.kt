package org.mozilla.social.common.appscope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class AppScope : CoroutineScope {

    private var supervisorJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = supervisorJob

    fun reset() {
        supervisorJob.cancel()
        supervisorJob = SupervisorJob()
    }
}