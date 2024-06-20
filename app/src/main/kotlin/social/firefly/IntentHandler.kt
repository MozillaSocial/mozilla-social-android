package social.firefly

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import social.firefly.core.accounts.AccountsManager
import social.firefly.core.navigation.Event
import social.firefly.core.navigation.EventRelay
import social.firefly.core.share.ShareInfo

class IntentHandler(
    private val eventRelay: EventRelay,
    private val accountsManager: AccountsManager,
) {

    fun handleIntent(intent: Intent) {
        CoroutineScope(Dispatchers.Default).launch {
            if (accountsManager.getAllAccounts().isEmpty()) return@launch
            when {
                intent.action == Intent.ACTION_SEND -> {
                    when {
                        intent.type == "text/plain" -> {
                            handleSendTextIntentReceived(intent)
                        }
                        intent.type?.contains("image") == true -> {
                            handleSendImageIntentReceived(intent)
                        }
                        intent.type?.contains("video") == true -> {
                            handleSendVideoIntentReceived(intent)
                        }
                    }
                }
            }
        }
    }

    private fun handleSendTextIntentReceived(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { sharedText ->
            ShareInfo.sharedText = sharedText
            eventRelay.emitEvent(Event.ChooseAccountForSharing)
        }
    }

    private fun handleSendImageIntentReceived(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let { sharedUri ->
            ShareInfo.sharedImageUri = sharedUri
            eventRelay.emitEvent(Event.ChooseAccountForSharing)
        }
    }

    private fun handleSendVideoIntentReceived(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let { sharedUri ->
            ShareInfo.sharedVideoUri = sharedUri
            eventRelay.emitEvent(Event.ChooseAccountForSharing)
        }
    }
}