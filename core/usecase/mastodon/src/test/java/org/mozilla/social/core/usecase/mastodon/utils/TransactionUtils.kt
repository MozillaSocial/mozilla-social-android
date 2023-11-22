package org.mozilla.social.core.usecase.mastodon.utils

import io.mockk.coEvery
import io.mockk.slot
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate

object TransactionUtils {
    fun setupTransactionMock(database: DatabaseDelegate) {
        val transactionLambda = slot<suspend () -> Any>()
        coEvery { database.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
    }
}
