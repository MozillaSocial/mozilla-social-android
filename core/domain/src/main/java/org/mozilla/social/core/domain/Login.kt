package org.mozilla.social.core.domain

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.HttpUrl
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.AppRepository
import org.mozilla.social.core.data.repository.OauthRepository
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.model.Account
import timber.log.Timber

/**
 * This use case handles all logic related to logging in
 */
class Login(
    private val oauthRepository: OauthRepository,
    private val accountRepository: AccountRepository,
    private val userPreferencesDatastore: UserPreferencesDatastore,
    private val appRepository: AppRepository,
) {

    val isSignedIn: Flow<Boolean>
        get() = userPreferencesDatastore.isSignedIn

    /**
     * When a  logging in by registering this client with the given domain
     */
    suspend operator fun invoke(context: Context, domain: String) {
        userPreferencesDatastore.saveDomain(domain)

        val application = appRepository.createApplication(
            clientName = CLIENT_NAME,
            redirectUris = REDIRECT_URI,
            scopes = SCOPES,
        )

        userPreferencesDatastore.saveClientCredentials(
            clientId = application.clientId!!,
            clientSecret = application.clientSecret!!
        )

        openLoginCustomTab(
            context = context, clientId = application.clientId!!, host = domain
        )
    }

    private fun openLoginCustomTab(context: Context, clientId: String, host: String) {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(
                context,
                HttpUrl.Builder()
                    .scheme(HTTPS)
                    .host(host)
                    .addPathSegments(OAUTH_AUTHORIZE)
                    .addQueryParameter(RESPONSE_TYPE_QUERY_PARAM, CODE)
                    .addQueryParameter(REDIRECT_URI_QUERY_PARAM, AUTH_SCHEME)
                    .addQueryParameter(SCOPE_QUERY_PARAM, READ_WRITE_PUSH)
                    .addQueryParameter(CLIENT_ID_QUERY_PARAM, clientId)
                    .build()
                    .toString()
                    .toUri()
            )
    }

    /**
     * Proceeds through the login flow if the intent fits the [AUTH_SCHEME] and contains a user code
     */
    suspend fun onNewIntentReceived(intent: Intent) {
        intent.data?.let { data ->
            if (data.toString().startsWith(AUTH_SCHEME)) {
                data.getQueryParameter(CODE)?.let { userCode ->
                    onUserCodeReceived(userCode)
                }
            }
        }
    }

    private suspend fun onUserCodeReceived(code: String) {
        Timber.tag(TAG).d("user code received")
        val token = oauthRepository.fetchOAuthToken(
            clientId = userPreferencesDatastore.clientId.first()!!,
            clientSecret = userPreferencesDatastore.clientSecret.first()!!,
            redirectUri = REDIRECT_URI,
            code = code,
            grantType = AUTHORIZATION_CODE,
        )

        onOAuthTokenReceived(token)
    }

    private suspend fun onOAuthTokenReceived(accessToken: String) {
        Timber.tag(TAG).d("access token received")
        userPreferencesDatastore.saveAccessToken(accessToken)
        val account: Account = accountRepository.verifyUserCredentials()
        userPreferencesDatastore.saveAccountId(accountId = account.accountId)
    }

    companion object {
        const val CLIENT_NAME = "MoSo Android"
        const val REDIRECT_URI = "mozsoc://auth"
        const val SCOPES = "read write push"
        const val AUTHORIZATION_CODE = "authorization_code"
        const val HTTPS = "https"
        const val OAUTH_AUTHORIZE = "oauth/authorize"
        const val AUTH_SCHEME = "mozsoc://auth"
        const val RESPONSE_TYPE_QUERY_PARAM = "response_type"
        const val CODE = "code"
        const val REDIRECT_URI_QUERY_PARAM = "redirect_uri"
        const val CLIENT_ID_QUERY_PARAM = "client_id"
        const val SCOPE_QUERY_PARAM = "scope"
        const val READ_WRITE_PUSH = "read write push"
        const val TAG = "Login"
    }
}