package social.firefly.core.usecase.mastodon.report

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.model.request.ReportCreate
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.ReportRepository
import social.firefly.core.usecase.mastodon.R

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
                body =
                    ReportCreate(
                        accountId = accountId,
                        statusIds = statusIds,
                        comment = comment,
                        forward = forward,
                        category = category,
                        ruleViolations = ruleViolations,
                    ),
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
