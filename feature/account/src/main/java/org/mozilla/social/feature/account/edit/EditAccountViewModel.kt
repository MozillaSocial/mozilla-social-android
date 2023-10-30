package org.mozilla.social.feature.account.edit

import android.net.Uri
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.Resource
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.domain.AccountIdBlocking
import timber.log.Timber
import java.io.File

class EditAccountViewModel(
    private val accountRepository: AccountRepository,
    accountIdBlocking: AccountIdBlocking,
) : ViewModel(), EditAccountInteractions {

    private val accountId = accountIdBlocking()

    private val _editAccountUiState = MutableStateFlow<Resource<EditAccountUiState>>(Resource.Loading())
    val editAccountUiState = _editAccountUiState.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading = _isUploading.asStateFlow()

    private val _errorToastMessage = MutableSharedFlow<StringFactory>(extraBufferCapacity = 1)
    val errorToastMessage = _errorToastMessage.asSharedFlow()

    private var newAvatar: File? = null
    private var newHeader: File? = null

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
        _isUploading.update { true }
        with(_editAccountUiState.value as? Resource.Loaded ?: return) {
            viewModelScope.launch {
                try {
                    accountRepository.updateMyAccount(
                        displayName = data.displayName.trim(),
                        bio = data.bio.trim(),
                        avatar = newAvatar,
                        header = newHeader,
                    )
                    //TODO navigate back
                } catch (e: Exception) {
                    //TODO show toast
                    Timber.e(e)
                    _isUploading.update { false }
                }
            }
        }
    }

    override fun onNewAvatarSelected(uri: Uri, file: File) {
        with(_editAccountUiState.value as? Resource.Loaded ?: return) {
            _editAccountUiState.update {
                Resource.Loaded(
                    data = data.copy(
                        avatarUrl = uri.toString()
                    )
                )
            }
        }
        newAvatar = file
    }

    override fun onNewHeaderSelected(uri: Uri, file: File) {
        with(_editAccountUiState.value as? Resource.Loaded ?: return) {
            _editAccountUiState.update {
                Resource.Loaded(
                    data = data.copy(
                        headerUrl = uri.toString()
                    )
                )
            }
        }
        newHeader = file
    }

    companion object {
        const val MAX_BIO_LENGTH = 500

        const val AVATAR_SIZE = 400
        const val HEADER_WIDTH = 1500
        const val HEADER_HEIGHT = 500
    }
}