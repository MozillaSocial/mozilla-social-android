@file:Suppress("detekt:all")
package org.mozilla.social.feature.report.step1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mozilla.social.common.logging.Log
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.data.repository.InstanceRepository
import org.mozilla.social.core.data.repository.ReportRepository
import org.mozilla.social.feature.report.R
import org.mozilla.social.feature.report.ReportInteractions
import org.mozilla.social.feature.report.ReportType
import org.mozilla.social.model.InstanceRule

class ReportScreen1ViewModel(
    private val reportRepository: ReportRepository,
    private val instanceRepository: InstanceRepository,
    private val log: Log,
    private val onNextClicked: (reportType: ReportType) -> Unit,
    private val onClose: () -> Unit,
    private val reportAccountId: String,
    private val reportAccountHandle: String,
    private val reportStatusId: String?,
) : ViewModel(), ReportInteractions {

    private val _selectedReportType = MutableStateFlow<ReportType?>(null)
    val selectedReportType = _selectedReportType.asStateFlow()

    private val _checkedRules = MutableStateFlow<List<InstanceRule>>(emptyList())
    val checkedRules = _checkedRules.asStateFlow()

    private val _instanceRules = MutableStateFlow<List<InstanceRule>>(emptyList())
    val instanceRules = _instanceRules.asStateFlow()

    private val _additionCommentText = MutableStateFlow("")
    val additionalCommentText = _additionCommentText.asStateFlow()

    private val _sendToExternalServerChecked = MutableStateFlow(false)
    val sendToExternalServerChecked = _sendToExternalServerChecked.asStateFlow()

    private val _errorToastMessage = MutableSharedFlow<StringFactory>(extraBufferCapacity = 1)
    val errorToastMessage = _errorToastMessage.asSharedFlow()

    init {
        loadInstanceRules()
    }

    private fun loadInstanceRules() {
        viewModelScope.launch {
            try {
                val rules = instanceRepository.getInstanceRules()
                _instanceRules.edit { rules }
            } catch (e: Exception) {
                log.e(e)
            }
        }
    }

    override fun onCloseClicked() {
        onClose()
    }

    //TODO move this to screen 2
    override fun onReportClicked() {
//        viewModelScope.launch {
//            try {
//                reportRepository.report(
//                    accountId = reportAccountId,
//                    statusIds = buildList {
//                        reportStatusId?.let { add(it) }
//                    },
//                    comment = additionalCommentText.value,
//                    category = selectedReportType.value?.stringValue,
//                    ruleViolations = instanceRules.value.map { it.id }
//                )
//            } catch (e: Exception) {
//                log.e(e)
//                _errorToastMessage.emit(StringFactory.resource(R.string.error_sending_report_toast))
//            }
//            onReported()
//        }
    }

    override fun onReportTypeSelected(reportType: ReportType) {
        _selectedReportType.edit { reportType }
        if (reportType != ReportType.VIOLATION) {
            _checkedRules.edit { emptyList() }
        }
    }

    override fun onServerRuleClicked(rule: InstanceRule) {
        _checkedRules.edit {
            buildList {
                addAll(checkedRules.value)
                if (checkedRules.value.contains(rule)) {
                    remove(rule)
                } else {
                    add(rule)
                }
            }
        }
    }

    override fun onAdditionCommentTextChanged(text: String) {
        _additionCommentText.edit { text }
    }

    override fun onSendToExternalServerClicked() {
        _sendToExternalServerChecked.edit { !sendToExternalServerChecked.value }
    }
}