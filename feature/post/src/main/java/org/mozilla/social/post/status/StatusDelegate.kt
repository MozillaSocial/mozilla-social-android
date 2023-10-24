package org.mozilla.social.post.status

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.logging.Log
import org.mozilla.social.common.utils.accountText
import org.mozilla.social.common.utils.edit
import org.mozilla.social.common.utils.hashtagText
import org.mozilla.social.common.utils.replaceAccount
import org.mozilla.social.common.utils.replaceHashtag
import org.mozilla.social.core.data.repository.SearchRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.post.NewPostViewModel
import org.mozilla.social.post.bottombar.BottomBarState

class StatusDelegate(
    private val coroutineScope: CoroutineScope,
    private val searchRepository: SearchRepository,
    private val statusRepository: StatusRepository,
    private val log: Log,
    private val inReplyToId: String?,
) : StatusInteractions, ContentWarningInteractions {

    private val _statusText = MutableStateFlow(TextFieldValue(""))
    val statusText = _statusText.asStateFlow()

    private val _accountList = MutableStateFlow<List<Account>?>(null)
    val accountList = _accountList.asStateFlow()

    private val _hashtagList = MutableStateFlow<List<String>?>(null)
    val hashtagList = _hashtagList.asStateFlow()

    private val _contentWarningText = MutableStateFlow<String?>(null)
    val contentWarningText = _contentWarningText.asStateFlow()

    private val _inReplyToAccountName = MutableStateFlow<String?>(null)
    val inReplyToAccountName = _inReplyToAccountName.asStateFlow()



    private var searchJob: Job? = null

    init {
        coroutineScope.launch {
            inReplyToId?.let {
                statusRepository.getStatusLocal(inReplyToId)?.account?.let { account ->
                    _statusText.update {
                        TextFieldValue(
                            text = "@${account.acct} ",
                            selection = TextRange(account.acct.length + 2)
                        )
                    }
                    _inReplyToAccountName.edit { account.username }
                }
            }
        }
    }

    override fun onStatusTextUpdated(textFieldValue: TextFieldValue) {
        if (textFieldValue.text.length + (contentWarningText.value?.length ?: 0) > NewPostViewModel.MAX_POST_LENGTH) return
        _statusText.update {
            log.d("updating onStatusTextUpdated")
            textFieldValue
        }

        searchForAccountsAndHashtags(textFieldValue)
    }

    private fun searchForAccountsAndHashtags(textFieldValue: TextFieldValue) {
        searchJob?.cancel()

        val accountText = textFieldValue.accountText()
        if (accountText.isNullOrBlank()) {
            _accountList.update { null }
        }

        val hashtagText = textFieldValue.hashtagText()
        if (hashtagText.isNullOrBlank()) {
            _hashtagList.update { null }
        }

        searchJob = coroutineScope.launch {
            // let the user stop typing before trying to search
            delay(500)

            if (!accountText.isNullOrBlank()) {
                try {
                    _accountList.update {
                        searchRepository.searchForAccounts(accountText).map {
                            Account(
                                accountId = it.acct,
                                profilePicUrl = it.avatarStaticUrl
                            )
                        }
                    }
                } catch (e: Exception) {
                    log.e(e)
                }
            }

            if (!hashtagText.isNullOrBlank()) {
                try {
                    _hashtagList.update {
                        searchRepository.searchForHashtags(hashtagText).map { it.name }
                    }
                } catch (e: Exception) {
                    log.e(e)
                }
            }
        }
    }

    override fun onAccountClicked(accountName: String) {
        _statusText.update { it.replaceAccount(accountName) }
        _accountList.update { null }
    }

    override fun onHashtagClicked(hashtag: String) {
        _statusText.update { it.replaceHashtag(hashtag) }
        _hashtagList.update { null }
    }

    override fun onContentWarningClicked() {
        if (contentWarningText.value == null) {
            _contentWarningText.update { "" }
        } else {
            _contentWarningText.update { null }
        }
    }

    override fun onContentWarningTextChanged(text: String) {
        if (text.length + statusText.value.text.length > NewPostViewModel.MAX_POST_LENGTH) return
        _contentWarningText.update { text }
    }
}
