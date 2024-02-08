package org.mozilla.social.core.usecase.mastodon.auth

import android.content.Intent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.core.analytics.AppAnalytics
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.AppRepository
import org.mozilla.social.core.repository.mastodon.OauthRepository
import org.mozilla.social.core.usecase.mastodon.auth.Login.Companion.AUTH_SCHEME
import timber.log.Timber

/**
 * This use case handles all logic related to logging in
 */
class Login(
    private val oauthRepository: OauthRepository,
    private val accountRepository: AccountRepository,
    private val userPreferencesDatastore: UserPreferencesDatastore,
    private val appRepository: AppRepository,
    private val analytics: AppAnalytics,
    private val openLink: OpenLink,
    private val logout: Logout,
) {
    private var clientId: String? = null
    private var clientSecret: String? = null

    /**
     * When a logging in by registering this client with the given domain
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(url: String) {
        try {
            val host = extractHost(url)
            userPreferencesDatastore.saveDomain(host)
            val application =
                appRepository.createApplication(
                    clientName = CLIENT_NAME,
                    redirectUris = REDIRECT_URI,
                    scopes = SCOPES,
                )
            clientId = application.clientId!!
            clientSecret = application.clientSecret!!
            openLink(
                HttpUrl.Builder()
                    .scheme(HTTPS)
                    .host(host)
                    .addPathSegments(OAUTH_AUTHORIZE)
                    .addQueryParameter(RESPONSE_TYPE_QUERY_PARAM, CODE)
                    .addQueryParameter(REDIRECT_URI_QUERY_PARAM, AUTH_SCHEME)
                    .addQueryParameter(SCOPE_QUERY_PARAM, READ_WRITE_PUSH)
                    .addQueryParameter(CLIENT_ID_QUERY_PARAM, application.clientId!!)
                    .build()
                    .toString(),
            )
        } catch (exception: Exception) {
            logout()
            throw LoginFailedException(exception)
        }
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

    @OptIn(PreferUseCase::class)
    private suspend fun onUserCodeReceived(code: String) {
        try {
            Timber.tag(TAG).d("user code received")
            val token =
                oauthRepository.fetchOAuthToken(
                    clientId = clientId!!,
                    clientSecret = clientSecret!!,
                    redirectUri = REDIRECT_URI,
                    code = code,
                    grantType = AUTHORIZATION_CODE,
                )
            onOAuthTokenReceived(token)
        } catch (exception: Exception) {
            logout()
            Timber.e(exception)
        }
    }

    private suspend fun onOAuthTokenReceived(accessToken: String) {
        Timber.tag(TAG).d("access token received")
        userPreferencesDatastore.saveAccessToken(accessToken)
        val account: Account = accountRepository.verifyUserCredentials()
        userPreferencesDatastore.saveAccountId(accountId = account.accountId)
        analytics.setMastodonAccountId(account.accountId)
        clientId = null
        clientSecret = null
    }

    private fun extractHost(domain: String): String {
        return buildString {
            append("https://")
            append(
                domain
                    .substringAfter("http://")
                    .substringAfter("https://")
            )
        }.toHttpUrl().host
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

    class LoginFailedException(e: Exception) : Exception(e)
}
