package org.mozilla.social.core.domain.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.data.repository.model.status.toDatabaseModel
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.domain.R
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.network.AccountApi
import java.io.File

class UpdateMyAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountApi: AccountApi,
    private val socialDatabase: SocialDatabase,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    /**
     * @throws UpdateAccountFailedException if any error occurred
     */
    suspend operator fun invoke(
        displayName: String? = null,
        bio: String? = null,
        locked: Boolean? = null,
        bot: Boolean? = null,
        avatar: File? = null,
        header: File? = null,
        fields: List<Pair<String, String>>? = null
    ) = externalScope.async(dispatcherIo) {
        try {
            println("johnny 1")
            val updatedAccount = accountApi.updateAccount(
                displayName = displayName?.toRequestBody(MultipartBody.FORM),
                bio = bio?.toRequestBody(MultipartBody.FORM),
                locked = locked?.toString()?.toRequestBody(MultipartBody.FORM),
                bot = bot?.toString()?.toRequestBody(MultipartBody.FORM),
                avatar = avatar?.let {
                    MultipartBody.Part.createFormData(
                        "avatar",
                        avatar.name,
                        avatar.asRequestBody("image/*".toMediaTypeOrNull()),
                    )
                },
                header = header?.let {
                    MultipartBody.Part.createFormData(
                        "header",
                        header.name,
                        header.asRequestBody("image/*".toMediaTypeOrNull()),
                    )
                },
                fieldLabel0 = fields?.getOrNull(0)?.first?.toRequestBody(MultipartBody.FORM),
                fieldContent0 = fields?.getOrNull(0)?.second?.toRequestBody(MultipartBody.FORM),
                fieldLabel1 = fields?.getOrNull(1)?.first?.toRequestBody(MultipartBody.FORM),
                fieldContent1 = fields?.getOrNull(1)?.second?.toRequestBody(MultipartBody.FORM),
                fieldLabel2 = fields?.getOrNull(2)?.first?.toRequestBody(MultipartBody.FORM),
                fieldContent2 = fields?.getOrNull(2)?.second?.toRequestBody(MultipartBody.FORM),
                fieldLabel3 = fields?.getOrNull(3)?.first?.toRequestBody(MultipartBody.FORM),
                fieldContent3 = fields?.getOrNull(3)?.second?.toRequestBody(MultipartBody.FORM),
            ).toExternalModel()
            println("johnny 2")
            socialDatabase.accountsDao().insert(updatedAccount.toDatabaseModel())
        } catch (e: Exception) {
            println("johnny 3 $e")
            showSnackbar(
                text = StringFactory.resource(R.string.edit_account_save_failed),
                isError = true,
            )
            throw UpdateAccountFailedException(e)
        }
    }.await()

    class UpdateAccountFailedException(e: Exception) : Exception(e)
}