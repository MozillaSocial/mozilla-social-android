package org.mozilla.social.core.usecase.mastodon.report

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.ReportRepository
import org.mozilla.social.core.usecase.mastodon.R
import org.mozilla.social.core.model.request.ReportCreate

class Report(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val reportRepository: ReportRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    /**
     * @throws ReportFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        accountId: String,
        statusIds: List<String>? = null,
        comment: String? = null,
        forward: Boolean? = null,
        category: String? = null,
        ruleViolations: List<Int>? = null,
    ) = externalScope.async(dispatcherIo) {
        try {
            reportRepository.report(
                body = ReportCreate(
                    accountId = accountId,
                    statusIds = statusIds,
                    comment = comment,
                    forward = forward,
                    category = category,
                    ruleViolations = ruleViolations
                )
            )
        } catch (e: Exception) {
            showSnackbar(
                text = StringFactory.resource(R.string.error_sending_report_toast),
                isError = true,
            )
            throw ReportFailedException(e)
        }
    }.await()

    class ReportFailedException(e: Exception) : Exception(e)
}