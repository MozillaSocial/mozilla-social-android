package org.mozilla.social.feature.report.step3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import timber.log.Timber

class ReportScreen3ViewModel(
    private val unfollowAccount: org.mozilla.social.core.usecase.mastodon.account.UnfollowAccount,
    private val blockAccount: org.mozilla.social.core.usecase.mastodon.account.BlockAccount,
    private val muteAccount: org.mozilla.social.core.usecase.mastodon.account.MuteAccount,
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
            } catch (e: org.mozilla.social.core.usecase.mastodon.account.UnfollowAccount.UnfollowFailedException) {
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
            } catch (e: org.mozilla.social.core.usecase.mastodon.account.MuteAccount.MuteFailedException) {
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
            } catch (e: org.mozilla.social.core.usecase.mastodon.account.BlockAccount.BlockFailedException) {
                Timber.e(e)
                _blockVisible.update { true }
            }
        }
    }
}