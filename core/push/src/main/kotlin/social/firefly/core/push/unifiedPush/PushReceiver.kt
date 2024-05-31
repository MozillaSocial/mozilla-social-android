package social.firefly.core.push.unifiedPush

import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent
import org.unifiedpush.android.connector.MessagingReceiver
import timber.log.Timber

class PushReceiver : MessagingReceiver(), KoinComponent {

    override fun onMessage(context: Context, message: ByteArray, instance: String) {
        super.onMessage(context, message, instance)
        Timber.tag(TAG).d("Message received: $message")
    }

    override fun onNewEndpoint(context: Context, endpoint: String, instance: String) {
        Timber.tag(TAG).d("new endpoint")
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