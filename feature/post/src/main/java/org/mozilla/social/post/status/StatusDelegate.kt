package org.mozilla.social.post.status

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.text.HtmlCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.findAccountAtCursor
import org.mozilla.social.common.utils.edit
import org.mozilla.social.common.utils.findHashtagAtCursor
import org.mozilla.social.common.utils.replaceAccount
import org.mozilla.social.common.utils.replaceHashtag
import org.mozilla.social.core.repository.mastodon.SearchRepository
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.analytics.NewPostAnalytics
import org.mozilla.social.post.NewPostViewModel
import timber.log.Timber

class StatusDelegate(
    private val analytics: NewPostAnalytics,
    private val searchRepository: SearchRepository,
    private val statusRepository: StatusRepository,
    private val coroutineScope: CoroutineScope,
    private val inReplyToId: String? = null,
    private val editStatusId: String? = null,
) : StatusInteractions, ContentWarningInteractions {

    private val _uiState = MutableStateFlow(StatusUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        coroutineScope.launch {
            inReplyToId?.let { populateReply(it) }
            editStatusId?.let { populateEditStatus(it) }
        }
    }

    private suspend fun populateReply(inReplyToId: String) {
        statusRepository.getStatusLocal(inReplyToId)?.account?.let { account ->
            _uiState.edit { copy(
                statusText = TextFieldValue(
                    text = "@${account.acct} ",
                    selection = TextRange(account.acct.length + 2),
                ),
                inReplyToAccountName = account.username
            ) }
        }
    }

    private suspend fun populateEditStatus(editStatusId: String) {
        statusRepository.getStatusLocal(editStatusId)?.let { status ->
            _uiState.edit { copy(
                statusText = TextFieldValue(
                    text = HtmlCompat.fromHtml(status.content, 0).toString()
                ),
                contentWarningText = status.contentWarningText.ifBlank { null },
                editStatusId = status.statusId,
            ) }
        }
    }

    override fun onStatusTextUpdated(textFieldValue: TextFieldValue) {
        if (textFieldValue.text.length +
            (uiState.value.contentWarningText?.length ?: 0)
            > NewPostViewModel.MAX_POST_LENGTH
        ) return
        _uiState.edit { copy(
            statusText = textFieldValue
        ) }

        searchForAccountsAndHashtags(textFieldValue)
    }

    private fun searchForAccountsAndHashtags(textFieldValue: TextFieldValue) {
        searchJob?.cancel()

        val accountText = textFieldValue.findAccountAtCursor()
        if (accountText.isNullOrBlank()) {
            _uiState.edit { copy(
                accountList = null
            ) }
        }

        val hashtagText = textFieldValue.findHashtagAtCursor()
        if (hashtagText.isNullOrBlank()) {
            _uiState.edit { copy(
                hashtagList = null
            ) }
        }

        searchJob =
            coroutineScope.launch {
                // let the user stop typing before trying to search
                delay(SEARCH_DELAY)

                if (!accountText.isNullOrBlank()) {
                    try {
                        val list = searchRepository.searchForAccounts(accountText).map {
                            Account(
                                accountId = it.acct,
                                profilePicUrl = it.avatarStaticUrl,
                            )
                        }
                        _uiState.edit { copy(
                            accountList = list
                        ) }
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }

                if (!hashtagText.isNullOrBlank()) {
                    try {
                        val list = searchRepository.searchForHashtags(hashtagText).map { it.name }
                        _uiState.edit { copy(
                            hashtagList = list
                        ) }
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
    }

    override fun onAccountClicked(accountName: String) {
        _uiState.edit { copy(
            statusText = uiState.value.statusText.replaceAccount(accountName),
            accountList = null,
        ) }
    }

    override fun onHashtagClicked(hashtag: String) {
        _uiState.edit { copy(
            statusText = uiState.value.statusText.replaceHashtag(hashtag),
            hashtagList = null,
        ) }
    }

    override fun onContentWarningClicked() {
        analytics.contentWarningClicked()
        if (uiState.value.contentWarningText == null) {
            _uiState.edit { copy(
                contentWarningText = ""
            ) }
        } else {
            _uiState.edit { copy(
                contentWarningText = null
            ) }
        }
    }

    override fun onContentWarningTextChanged(text: String) {
        if (text.length + uiState.value.statusText.text.length > NewPostViewModel.MAX_POST_LENGTH) return
        _uiState.edit { copy(
            contentWarningText = text
        ) }
    }

    companion object {
        const val SEARCH_DELAY = 500L
    }
}
