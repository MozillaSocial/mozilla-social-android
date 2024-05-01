package social.firefly.core.usecase.mastodon.utils

import io.mockk.coEvery
import io.mockk.slot
import social.firefly.core.repository.mastodon.DatabaseDelegate

object TransactionUtils {
    fun setupTransactionMock(database: DatabaseDelegate) {
        val transactionLambda = slot<suspend () -> Any>()
        coEvery { database.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
    }
}
