package org.mozilla.social.feature.account.edit

import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.Resource
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.domain.AccountIdBlocking
import timber.log.Timber

class EditAccountViewModel(
    private val accountRepository: AccountRepository,
    accountIdBlocking: AccountIdBlocking,
) : ViewModel(), EditAccountInteractions {

    private val accountId = accountIdBlocking()

    private val _editAccountUiState = MutableStateFlow<Resource<EditAccountUiState>>(Resource.Loading())
    val editAccountUiState = _editAccountUiState.asStateFlow()

    init {
        loadAccount()
    }

    private fun loadAccount() {
        viewModelScope.launch {
            try {
                val account = accountRepository.getAccountFromDatabase(accountId)!!
                val bio = HtmlCompat.fromHtml(account.bio, 0).toString()
                _editAccountUiState.update {
                    Resource.Loaded(
                        data = EditAccountUiState(
                            topBarTitle = account.displayName,
                            headerUrl = account.headerUrl,
                            avatarUrl = account.avatarUrl,
                            handle = "@${account.acct}",
                            displayName = account.displayName,
                            bio = bio,
                            bioCharacterCount = bio.length,
                        )
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onDisplayNameTextChanged(text: String) {
        with(_editAccountUiState.value as? Resource.Loaded ?: return) {
            _editAccountUiState.update {
                Resource.Loaded(
                    data = data.copy(
                        displayName = text
                    )
                )
            }
        }
    }

    override fun onBioTextChanged(text: String) {
        if (text.length > MAX_BIO_LENGTH) return
        with(_editAccountUiState.value as? Resource.Loaded ?: return) {
            _editAccountUiState.update {
                Resource.Loaded(
                    data = data.copy(
                        bio = text,
                        bioCharacterCount = text.length
                    )
                )
            }
        }
    }

    override fun onSaveClicked() {
        viewModelScope.launch {

        }
    }

    companion object {
        const val MAX_BIO_LENGTH = 500
    }
}