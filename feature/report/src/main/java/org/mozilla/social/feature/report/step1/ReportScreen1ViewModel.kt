@file:Suppress("detekt:all")
package org.mozilla.social.feature.report.step1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.data.repository.InstanceRepository
import org.mozilla.social.feature.report.ReportDataBundle
import org.mozilla.social.feature.report.ReportType
import org.mozilla.social.model.InstanceRule
import timber.log.Timber

class ReportScreen1ViewModel(
    private val instanceRepository: InstanceRepository,
    private val onNextClicked: (reportType: ReportType, serializedReportData: String) -> Unit,
    private val onClose: () -> Unit,
    private val reportAccountId: String,
    private val reportAccountHandle: String,
    private val reportStatusId: String?,
) : ViewModel(), ReportScreen1Interactions {

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

    init {
        loadInstanceRules()
    }

    private fun loadInstanceRules() {
        viewModelScope.launch {
            try {
                val rules = instanceRepository.getInstanceRules()
                _instanceRules.edit { rules }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onCloseClicked() {
        onClose()
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

    override fun onNextClicked() {
        val bundle = ReportDataBundle(
            reportAccountId = reportAccountId,
            reportAccountHandle = reportAccountHandle,
            reportStatusId = reportStatusId,
            reportType = selectedReportType.value ?: ReportType.DO_NOT_LIKE,
            checkedInstanceRules = checkedRules.value,
            additionalText = additionalCommentText.value,
            sendToExternalServer = sendToExternalServerChecked.value,
        )
        onNextClicked(bundle.reportType, Json.encodeToString(bundle))
    }
}