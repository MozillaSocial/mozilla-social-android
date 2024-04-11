package social.firefly.core.push

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.unifiedpush.android.connector.MessagingReceiver
import social.firefly.common.appscope.AppScope
import social.firefly.core.repository.mastodon.PushRepository
import timber.log.Timber

class PushReceiver : MessagingReceiver(), KoinComponent {

    private val keyManager: KeyManager by inject()

    private val pushRepository: PushRepository by inject()

    private val coroutineScope: AppScope by inject()

    override fun onMessage(context: Context, message: ByteArray, instance: String) {
        super.onMessage(context, message, instance)
        Timber.tag(TAG).d("Message received: $message")
    }

    override fun onNewEndpoint(context: Context, endpoint: String, instance: String) {
        Timber.tag(TAG).d("new endpoint")
        coroutineScope.launch {
            val keysDeferred = CompletableDeferred<EncodedPushKeys>()

            launch {
                keyManager.encodedPushKeys.collectLatest { keysDeferred.complete(it) }
            }

            val keys = keysDeferred.await()

            Timber.tag(TAG).d("keys: $keys")

            try {
                val webPushSubscription = pushRepository.subscribe(
                    endpoint = endpoint,
                    p256dh = keys.publicKey,
                    auth = keys.authSecret,
                )
                Timber.tag(TAG).d("Web push subscription: $webPushSubscription")
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Timber.tag(TAG).d("onReceive")
    }

    override fun onRegistrationFailed(context: Context, instance: String) {
        super.onRegistrationFailed(context, instance)
        Timber.tag(TAG).d("registration failed")
    }

    override fun onUnregistered(context: Context, instance: String) {
        super.onUnregistered(context, instance)
        Timber.tag(TAG).d("unregistered")
    }

    companion object {
        private val TAG = PushReceiver::class.simpleName!!
    }
}