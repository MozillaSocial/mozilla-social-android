package social.firefly.core.share

import android.net.Uri

/**
 * There's not a great way to share strings of unknown origin in jetpack compose navigation.
 * A string could contains "/" which could mess up the navigation route.
 * This is a workaround that just holds the shared string in an easy place we can access when
 * we need it.
 */
object ShareInfo {
    // you can get this text once, then it's gone
    var sharedText: String? = null
        get() {
            val returnValue = field
            field = null
            return returnValue
        }

    var sharedImageUri: Uri? = null
        get() {
            val returnValue = field
            field = null
            return returnValue
        }

    var sharedVideoUri: Uri? = null
        get() {
            val returnValue = field
            field = null
            return returnValue
        }
}