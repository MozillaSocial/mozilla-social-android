@file:Suppress("detekt:all")
package org.mozilla.social.feature.report.step2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.data.repository.ReportRepository
import org.mozilla.social.feature.report.R
import org.mozilla.social.feature.report.ReportType
import org.mozilla.social.model.InstanceRule
import timber.log.Timber

class ReportScreen2ViewModel(
    private val reportRepository: ReportRepository,
    private val onCloseClicked: () -> Unit,
    private val onReportSubmitted: () -> Unit,
    private val reportAccountId: String,
    private val reportAccountHandle: String,
    private val reportStatusId: String?,
    private val reportType: ReportType,
    private val checkedInstanceRules: List<InstanceRule>,
    private val additionalText: String,
    private val sendToExternalServer: Boolean,
) : ViewModel() {

    private val _errorToastMessage = MutableSharedFlow<StringFactory>(extraBufferCapacity = 1)
    val errorToastMessage = _errorToastMessage.asSharedFlow()

    fun onReportClicked() {
        viewModelScope.launch {
            try {
                reportRepository.report(
                    accountId = reportAccountId,
                    statusIds = buildList {
                        reportStatusId?.let { add(it) }
                    },
                    comment = additionalText,
                    category = reportType.stringValue,
                    ruleViolations = checkedInstanceRules.map { it.id },
                    forward = sendToExternalServer,
                )
            } catch (e: Exception) {
                Timber.e(e)
                _errorToastMessage.emit(StringFactory.resource(R.string.error_sending_report_toast))
            }
            onReportSubmitted()
        }
    }
}