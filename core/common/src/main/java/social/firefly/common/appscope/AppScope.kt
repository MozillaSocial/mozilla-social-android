package social.firefly.common.appscope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 * A coroutine scope that uses a supervisor job as it's context.
 *
 * In Ff, we are only canceling the supervisor job on logout.
 */
class AppScope : CoroutineScope {
    private var supervisorJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = supervisorJob

    /**
     * Create a new supervisor and cancel the old one
     */
    fun reset() {
        val oldSupervisor = supervisorJob
        supervisorJob = SupervisorJob()
        oldSupervisor.cancel()
    }
}
