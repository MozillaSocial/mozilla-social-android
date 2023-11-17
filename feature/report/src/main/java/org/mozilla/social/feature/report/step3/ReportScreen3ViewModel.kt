package org.mozilla.social.feature.report.step3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.core.usecase.mastodon.account.BlockAccount
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.MuteAccount
import org.mozilla.social.core.usecase.mastodon.account.UnfollowAccount
import timber.log.Timber

class ReportScreen3ViewModel(
    private val unfollowAccount: UnfollowAccount,
    private val blockAccount: BlockAccount,
    private val muteAccount: MuteAccount,
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
                unfollowAccount(
                    accountId = reportAccountId,
                    loggedInUserAccountId = usersAccountId,
                )
            } catch (e: UnfollowAccount.UnfollowFailedException) {
                Timber.e(e)
                _unfollowVisible.update { true }
            }
        }
    }

    override fun onMuteClicked() {
        _muteVisible.update { false }
        viewModelScope.launch {
            try {
                muteAccount(reportAccountId)
            } catch (e: MuteAccount.MuteFailedException) {
                Timber.e(e)
                _muteVisible.update { true }
            }
        }
    }

    override fun onBlockClicked() {
        _blockVisible.update { false }
        viewModelScope.launch {
            try {
                blockAccount(reportAccountId)
            } catch (e: BlockAccount.BlockFailedException) {
                Timber.e(e)
                _blockVisible.update { true }
            }
        }
    }
}
