package org.mozilla.social.feature.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mozilla.social.common.logging.Log
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.data.repository.InstanceRepository
import org.mozilla.social.core.data.repository.ReportRepository
import org.mozilla.social.model.InstanceRule

class ReportViewModel(
    private val reportRepository: ReportRepository,
    private val instanceRepository: InstanceRepository,
    private val log: Log,
    private val onReported: () -> Unit,
    private val onClose: () -> Unit,
    private val reportAccountId: String,
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

    override fun onReportClicked() {
        viewModelScope.launch {
            try {
                reportRepository.report(
                    accountId = reportAccountId,
                    statusIds = buildList {
                        reportStatusId?.let { add(it) }
                    },
                    comment = additionalCommentText.value,
                    category = selectedReportType.value?.stringValue,
                    ruleViolations = instanceRules.value.map { it.id }
                )
            } catch (e: Exception) {
                log.e(e)
            }
            onReported()
        }
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
}