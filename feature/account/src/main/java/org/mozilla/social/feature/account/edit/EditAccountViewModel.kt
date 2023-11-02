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
import org.mozilla.social.core.navigation.usecases.PopNavBackstack
import org.mozilla.social.feature.account.R
import timber.log.Timber
import java.io.File

class EditAccountViewModel(
    private val accountRepository: AccountRepository,
    accountIdBlocking: AccountIdBlocking,
    private val popNavBackstack: PopNavBackstack,
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
        _editAccountUiState.update { Resource.Loading() }
        viewModelScope.launch {
            try {
                val account = accountRepository.getAccountFromDatabase(accountId)!!
                _editAccountUiState.update {
                    Resource.Loaded(
                        data = account.toUiState()
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
                _editAccountUiState.update { Resource.Error(e) }
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
                        locked = data.lockChecked,
                        bot = data.botChecked,
                        fields = data.fields.map {
                            Pair(
                                first = it.label,
                                second = it.content,
                            )
                        }
                    )
                    popNavBackstack()
                } catch (e: Exception) {
                    _errorToastMessage.emit(StringFactory.resource(R.string.edit_account_save_failed))
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

    override fun onLockClicked() {
        with(_editAccountUiState.value as? Resource.Loaded ?: return) {
            _editAccountUiState.update {
                Resource.Loaded(
                    data = data.copy(
                        lockChecked = !data.lockChecked
                    )
                )
            }
        }
    }

    override fun onBotClicked() {
        with(_editAccountUiState.value as? Resource.Loaded ?: return) {
            _editAccountUiState.update {
                Resource.Loaded(
                    data = data.copy(
                        botChecked = !data.botChecked
                    )
                )
            }
        }
    }

    override fun onRetryClicked() {
        loadAccount()
    }

    override fun onLabelTextChanged(index: Int, text: String) {
        with(_editAccountUiState.value as? Resource.Loaded ?: return) {
            _editAccountUiState.update {
                Resource.Loaded(
                    data = data.copy(
                        fields = data.fields
                    )
                )
            }
        }
    }

    override fun onContentTextChanged(index: Int, text: String) {
        super.onContentTextChanged(index, text)
    }

    override fun onFieldDeleteClicked(index: Int) {
        super.onFieldDeleteClicked(index)
    }

    override fun onAddFieldClicked() {
        super.onAddFieldClicked()
    }

    companion object {
        const val MAX_BIO_LENGTH = 500
    }
}