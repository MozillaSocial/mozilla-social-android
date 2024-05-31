package social.firefly.core.push.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import social.firefly.common.appscope.AppScope
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.push.KeyManager
import timber.log.Timber

class FcmService : FirebaseMessagingService(), KoinComponent {

    private val keyManager: KeyManager by inject()
//    private val pushRepository: PushRepository by inject()
    private val coroutineScope: AppScope by inject()
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager by inject()

    private val tag = FcmService::class.simpleName!!

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.tag(tag).d("message received: $message")
        //TODO decrypt message with specific account's keys and show message
    }

    override fun onNewToken(token: String) {
        Timber.tag(tag).d("new token: $token")

        userPreferencesDatastoreManager.dataStores.value.forEach { userPrefsDatastore ->
            coroutineScope.launch {
                val keys = keyManager.generatePushKeys()
                userPrefsDatastore.saveSerializedPushKeyPair(Json.encodeToString(keys))

                Timber.tag(tag).d("keys: $keys")

                try {
                    //TODO update endpoint
//                val webPushSubscription = pushRepository.subscribe(
//                    endpoint = endpoint,
//                    p256dh = keys.publicKey,
//                    auth = keys.authSecret,
//                )
//                Timber.tag(tag).d("Web push subscription: $webPushSubscription")
                } catch (e: Exception) {
                    Timber.tag(tag).e(e)
                }
            }
        }
    }
}