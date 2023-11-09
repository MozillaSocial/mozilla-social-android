package org.mozilla.social.feature.report.step3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.domain.GetLoggedInUserAccountId
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.feature.report.R
import timber.log.Timber

class ReportScreen3ViewModel(
    private val accountRepository: AccountRepository,
    private val showSnackbar: ShowSnackbar,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    private val doneClicked: () -> Unit,
    private val closeClicked: () -> Unit,
    private val reportAccountId: String,
) : ViewModel(), ReportScreen3Interactions {

    /**
     * The account ID of the logged in user
     */
    private val usersAccountId: String = getLoggedInUserAccountId()

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
                accountRepository.unfollowAccount(
                    accountId = reportAccountId,
                    loggedInUserAccountId = usersAccountId,
                )
            } catch (e: Exception) {
                Timber.e(e)
                showSnackbar(
                    text = StringFactory.resource(R.string.error_unfollowing),
                    isError = true,
                )
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
                showSnackbar(
                    text = StringFactory.resource(R.string.error_muting),
                    isError = true,
                )
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
                showSnackbar(
                    text = StringFactory.resource(R.string.error_blocking),
                    isError = true,
                )
                _blockVisible.update { true }
            }
        }
    }
}