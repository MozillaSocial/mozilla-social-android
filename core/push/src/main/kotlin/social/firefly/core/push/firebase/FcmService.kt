package social.firefly.core.push.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class FcmService : FirebaseMessagingService() {

    private val tag = FcmService::class.simpleName!!

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.tag(tag).d("message received: $message")
    }

    override fun onNewToken(token: String) {
        Timber.tag(tag).d("new token: $token")
    }
}