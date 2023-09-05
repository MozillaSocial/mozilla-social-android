package org.mozilla.social.feature.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportViewModel(
    private val onReported: () -> Unit,
    private val onClose: () -> Unit,
    private val reportAccountId: String,
    private val reportStatusId: String?,
) : ViewModel(), ReportInteractions {

    private val _selectedReportType = MutableStateFlow<ReportType?>(null)
    val selectedReportType = _selectedReportType.asStateFlow()

    override fun onCloseClicked() {
        onClose()
    }

    override fun onReportClicked() {
        viewModelScope.launch {
            try {

            } catch (e: Exception) {

            }
            onReported()
        }
    }
}