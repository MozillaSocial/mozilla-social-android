package org.mozilla.social.core.domain

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex

/**
 * The account ID of the logged in user
 *
 * So much depends on this value, trying out a blocking call to simplify the rest of the code.
 *
 * Uses a mutex so we can cancel the flow collection after we get the value.
 */
class AccountIdBlocking(
    private val accountIdFlow: AccountIdFlow,
) {

    operator fun invoke(): String = runBlocking {
        var value = ""

        val mutex = Mutex(true)
        val job = launch {
            accountIdFlow().collect {
                value = it
                mutex.unlock()
            }
        }
        mutex.lock()
        job.cancel()

        value
    }
}