package social.firefly.feature.report.step2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import social.firefly.common.Resource
import social.firefly.core.model.InstanceRule
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.usecase.mastodon.report.Report
import social.firefly.feature.report.ReportDataBundle
import social.firefly.feature.report.ReportType
import timber.log.Timber

class ReportScreen2ViewModel(
    private val report: Report,
    private val accountRepository: AccountRepository,
    private val onClose: () -> Unit,
    private val onReportSubmitted: (bundle: ReportDataBundle.ReportDataBundleForScreen3) -> Unit,
    private val reportAccountId: String,
    private val reportAccountHandle: String,
    private val reportStatusId: String?,
    private val reportType: ReportType,
    private val checkedInstanceRules: List<InstanceRule>,
    private val additionalText: String,
    private val sendToExternalServer: Boolean,
) : ViewModel(), ReportScreen2Interactions {
    private val _statuses =
        MutableStateFlow<Resource<List<ReportStatusUiState>>>(Resource.Loading())
    val statuses = _statuses.asStateFlow()

    private val _reportIsSending = MutableStateFlow(false)
    val reportIsSending = _reportIsSending.asStateFlow()

    init {
        getStatuses()
    }

    private fun getStatuses() {
        _statuses.update { Resource.Loading() }
        viewModelScope.launch {
            try {
                val uiStateList =
                    accountRepository.getAccountStatuses(
                        accountId = reportAccountId,
                        loadSize = 40,
                        excludeBoosts = true,
                    ).items.map {
                        it.toReportStatusUiState()
                    }.filterNot {
                        it.statusId == reportStatusId
                    }
                _statuses.update {
                    Resource.Loaded(uiStateList)
                }
            } catch (e: Exception) {
                Timber.e(e)
                _statuses.update { Resource.Error(e) }
            }
        }
    }

    override fun onReportClicked() {
        _reportIsSending.update { true }
        viewModelScope.launch {
            try {
                report(
                    accountId = reportAccountId,
                    statusIds =
                    buildList {
                        reportStatusId?.let { add(it) }
                        (statuses.value as? Resource.Loaded)?.data?.forEach {
                            if (it.checked) add(it.statusId)
                        }
                    },
                    comment = additionalText,
                    category = reportType.stringValue,
                    ruleViolations = checkedInstanceRules.map { it.id },
                    forward = sendToExternalServer,
                )
                onReportSubmitted(
                    ReportDataBundle.ReportDataBundleForScreen3(
                        reportAccountId = reportAccountId,
                        reportAccountHandle = reportAccountHandle,
                        didUserReportAccount = true,
                    ),
                )
            } catch (e: Report.ReportFailedException) {
                Timber.e(e)
                _reportIsSending.update { false }
            }
        }
    }

    override fun onCloseClicked() {
        onClose()
    }

    override fun onStatusClicked(statusId: String) {
        _statuses.update {
            Resource.Loaded(
                data =
                (statuses.value as Resource.Loaded).data.map {
                    if (it.statusId == statusId) {
                        it.copy(
                            checked = !it.checked,
                        )
                    } else {
                        it
                    }
                },
            )
        }
    }

    override fun onRetryClicked() {
        getStatuses()
    }
}
