package org.mozilla.social.feature.account.edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.Resource
import org.mozilla.social.common.updateData
import org.mozilla.social.core.navigation.usecases.PopNavBackstack
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.UpdateMyAccount
import org.mozilla.social.core.analytics.AccountAnalytics
import timber.log.Timber
import java.io.File

class EditAccountViewModel(
    private val accountRepository: AccountRepository,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    private val popNavBackstack: PopNavBackstack,
    private val updateMyAccount: UpdateMyAccount,
    private val analytics: AccountAnalytics,
) : ViewModel(), EditAccountInteractions {
    private val accountId = getLoggedInUserAccountId()

    private val _editAccountUiState = MutableStateFlow<Resource<EditAccountUiState>>(Resource.Loading())
    val editAccountUiState = _editAccountUiState.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading = _isUploading.asStateFlow()

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
                        data = account.toUiState(),
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
                _editAccountUiState.update { Resource.Error(e) }
            }
        }
    }

    override fun onSaveClicked() {
        _isUploading.update { true }
        with(_editAccountUiState.value as? Resource.Loaded ?: return) {
            viewModelScope.launch {
                try {
                    analytics.editAccountSaved()
                    updateMyAccount(
                        displayName = data.displayName.trim(),
                        bio = data.bio.trim(),
                        avatar = newAvatar,
                        header = newHeader,
                        locked = data.lockChecked,
                        bot = data.botChecked,
                        fields =
                            data.fields.map {
                                Pair(
                                    first = it.label,
                                    second = it.content,
                                )
                            },
                    )
                    popNavBackstack()
                } catch (e: UpdateMyAccount.UpdateAccountFailedException) {
                    Timber.e(e)
                    _isUploading.update { false }
                }
            }
        }
    }

    override fun onDisplayNameTextChanged(text: String) {
        _editAccountUiState.updateData {
            copy(
                displayName = text,
            )
        }
    }

    override fun onBioTextChanged(text: String) {
        if (text.length > MAX_BIO_LENGTH) return
        _editAccountUiState.updateData {
            copy(
                bio = text,
                bioCharacterCount = text.length,
            )
        }
    }

    override fun onNewAvatarSelected(
        uri: Uri,
        file: File,
    ) {
        _editAccountUiState.updateData {
            copy(
                avatarUrl = uri.toString(),
            )
        }
        newAvatar = file
    }

    override fun onNewHeaderSelected(
        uri: Uri,
        file: File,
    ) {
        _editAccountUiState.updateData {
            copy(
                headerUrl = uri.toString(),
            )
        }
        newHeader = file
    }

    override fun onLockClicked() {
        _editAccountUiState.updateData {
            copy(
                lockChecked = !lockChecked,
            )
        }
    }

    override fun onBotClicked() {
        _editAccountUiState.updateData {
            copy(
                botChecked = !botChecked,
            )
        }
    }

    override fun onRetryClicked() {
        loadAccount()
    }

    override fun onLabelTextChanged(
        index: Int,
        text: String,
    ) {
        if (text.length > MAX_FIELD_LENGTH) return
        _editAccountUiState.updateData {
            copy(
                fields =
                    fields.toMutableList().apply {
                        val content = get(index).content
                        removeAt(index)
                        add(
                            index,
                            EditAccountUiStateField(
                                label = text,
                                content = content,
                            ),
                        )
                        modifyFieldCount()
                    },
            )
        }
    }

    override fun onContentTextChanged(
        index: Int,
        text: String,
    ) {
        if (text.length > MAX_FIELD_LENGTH) return
        _editAccountUiState.updateData {
            copy(
                fields =
                    fields.toMutableList().apply {
                        val label = get(index).label
                        removeAt(index)
                        add(
                            index,
                            EditAccountUiStateField(
                                label = label,
                                content = text,
                            ),
                        )
                        modifyFieldCount()
                    },
            )
        }
    }

    override fun onFieldDeleteClicked(index: Int) {
        _editAccountUiState.updateData {
            copy(
                fields =
                    fields.toMutableList().apply {
                        removeAt(index)
                    },
            )
        }
    }

    override fun onAddFieldClicked() {
        _editAccountUiState.updateData {
            copy(
                fields =
                    fields.toMutableList().apply {
                        add(EditAccountUiStateField("", ""))
                    },
            )
        }
    }

    override fun onScreenViewed() {
        analytics.editAccountScreenViewed()
    }

    private fun MutableList<EditAccountUiStateField>.modifyFieldCount() {
        if (size < MAX_FIELDS && (last().label.isNotBlank() || last().content.isNotBlank())) {
            add(EditAccountUiStateField("", ""))
        }
    }

    companion object {
        const val MAX_BIO_LENGTH = 500
        const val MAX_FIELDS = 4
        const val MAX_FIELD_LENGTH = 255
    }
}
