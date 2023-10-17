@file:Suppress("detekt:all")
package org.mozilla.social.feature.report.step3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.feature.report.R
import timber.log.Timber

class ReportScreen3ViewModel(
    private val accountRepository: AccountRepository,
    private val doneClicked: () -> Unit,
    private val closeClicked: () -> Unit,
    private val reportAccountId: String,
) : ViewModel(), ReportScreen3Interactions {

    private val _errorToastMessage = MutableSharedFlow<StringFactory>(extraBufferCapacity = 1)
    val errorToastMessage = _errorToastMessage.asSharedFlow()

    private val _unfollowVisible = MutableStateFlow(true)
    val unfollowVisible = _unfollowVisible.asStateFlow()

    private val _muteVisible = MutableStateFlow(true)
    val muteVisible = _muteVisible.asStateFlow()

    private val _blockVisible = MutableStateFlow(true)
    val blockVisible = _blockVisible.asStateFlow()

    override fun onCloseClicked() {
        closeClicked()
    }

    override fun onDoneClicked() {
        doneClicked()
    }

    override fun onUnfollowClicked() {
        _unfollowVisible.update { false }
        viewModelScope.launch {
            try {
                accountRepository.unfollowAccount(reportAccountId)
            } catch (e: Exception) {
                Timber.e(e)
                _errorToastMessage.emit(StringFactory.resource(R.string.error_unfollowing))
                _unfollowVisible.update { true }
            }
        }
    }

    override fun onMuteClicked() {
        _muteVisible.update { false }
        viewModelScope.launch {
            try {
                accountRepository.muteAccount(reportAccountId)
            } catch (e: Exception) {
                Timber.e(e)
                _errorToastMessage.emit(StringFactory.resource(R.string.error_muting))
                _muteVisible.update { true }
            }
        }
    }

    override fun onBlockClicked() {
        _blockVisible.update { false }
        viewModelScope.launch {
            try {
                accountRepository.blockAccount(reportAccountId)
            } catch (e: Exception) {
                Timber.e(e)
                _errorToastMessage.emit(StringFactory.resource(R.string.error_blocking))
                _blockVisible.update { true }
            }
        }
    }
}