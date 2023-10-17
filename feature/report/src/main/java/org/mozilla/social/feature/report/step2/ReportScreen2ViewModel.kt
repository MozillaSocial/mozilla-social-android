package org.mozilla.social.feature.report.step2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.Resource
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.ReportRepository
import org.mozilla.social.feature.report.R
import org.mozilla.social.feature.report.ReportDataBundle
import org.mozilla.social.feature.report.ReportType
import org.mozilla.social.model.InstanceRule
import timber.log.Timber

class ReportScreen2ViewModel(
    private val accountRepository: AccountRepository,
    private val reportRepository: ReportRepository,
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

    private val _errorToastMessage = MutableSharedFlow<StringFactory>(extraBufferCapacity = 1)
    val errorToastMessage = _errorToastMessage.asSharedFlow()

    private val _statuses = MutableStateFlow<Resource<List<ReportStatusUiState>>>(Resource.Loading())
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
                val uiStateList = accountRepository.getAccountStatuses(
                    accountId = reportAccountId,
                    loadSize = 40,
                    excludeBoosts = true,
                ).map {
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
                reportRepository.report(
                    accountId = reportAccountId,
                    statusIds = buildList {
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
                    )
                )
            } catch (e: Exception) {
                Timber.e(e)
                _errorToastMessage.emit(StringFactory.resource(R.string.error_sending_report_toast))
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
                data = (statuses.value as Resource.Loaded).data.map {
                    if (it.statusId == statusId) {
                        it.copy(
                            checked = !it.checked
                        )
                    } else {
                        it
                    }
                }
            )
        }
    }

    override fun onRetryClicked() {
        getStatuses()
    }
}