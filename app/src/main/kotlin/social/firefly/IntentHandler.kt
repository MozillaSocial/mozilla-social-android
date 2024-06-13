package social.firefly

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.navigation.Event
import social.firefly.core.navigation.EventRelay
import social.firefly.core.share.ShareInfo

class IntentHandler(
    private val eventRelay: EventRelay,
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
) {

    fun handleIntent(intent: Intent) {
        if (!userPreferencesDatastoreManager.isLoggedInToAtLeastOneAccount) return
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