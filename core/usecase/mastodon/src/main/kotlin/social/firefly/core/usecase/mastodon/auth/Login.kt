package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.datastore.AlreadySignedInException
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.model.Account
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.navigation.usecases.OpenLink
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.VerificationRepository
import social.firefly.core.usecase.mastodon.R
import timber.log.Timber

/**
 * This use case handles all logic related to logging in
 */
class Login(
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val openLink: OpenLink,
    private val navigateTo: NavigateTo,
    private val databaseDelegate: DatabaseDelegate,
    private val showSnackbar: ShowSnackbar,
): KoinComponent {
    private lateinit var clientId: String
    private lateinit var clientSecret: String
    private lateinit var host: String

    private lateinit var verificationRepository: VerificationRepository
    /**
     * When a logging in by registering this client with the given domain
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(url: String) {
        try {
            host = extractHost(url)
            verificationRepository = getKoin().get<VerificationRepository> {
                parametersOf("https://$host")
            }
            val application = verificationRepository.createApplication(
                clientName = CLIENT_NAME,
                redirectUris = REDIRECT_URI,
                scopes = SCOPES,
                baseUrl = host,
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
                    .addQueryParameter(SCOPE_QUERY_PARAM, SCOPES)
                    .addQueryParameter(CLIENT_ID_QUERY_PARAM, application.clientId!!)
                    .build()
                    .toString(),
            )
        } catch (exception: Exception) {
            throw LoginFailedException(exception)
        }
    }

    @OptIn(PreferUseCase::class)
    suspend fun onUserCodeReceived(code: String) {
        try {
            Timber.tag(TAG).d("user code received")
            val accessToken = verificationRepository.fetchOAuthToken(
                clientId = clientId,
                clientSecret = clientSecret,
                redirectUri = REDIRECT_URI,
                code = code,
                grantType = AUTHORIZATION_CODE,
                baseUrl = host,
            )

            Timber.tag(TAG).d("access token received")

            val account: Account = verificationRepository.verifyUserCredentials(
                accessToken = accessToken,
                baseUrl = host,
            )
            val defaultLanguage = account.source?.defaultLanguage ?: ""
            userPreferencesDatastoreManager.createNewUserDatastore(
                domain = host,
                accessToken = accessToken,
                accountId = account.accountId,
                userName = account.displayName,
                avatarUrl = account.avatarUrl,
                defaultLanguage = defaultLanguage,
            )
            withContext(Dispatchers.IO) {
                databaseDelegate.clearAllTables()
            }
            navigateTo(NavigationDestination.Tabs)
        } catch (e: AlreadySignedInException) {
            showSnackbar(
                text = StringFactory.resource(R.string.already_signed_in_to_account_error),
                isError = true,
            )
            Timber.e(e)
        } catch (exception: Exception) {
            showSnackbar(
                text = StringFactory.resource(R.string.error_signing_in),
                isError = true,
            )
            Timber.e(exception)
        }
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
        const val CLIENT_NAME = "Mozilla Social Android"
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
        const val TAG = "Login"
    }

    class LoginFailedException(e: Exception) : Exception(e)
}
