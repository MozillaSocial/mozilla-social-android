package org.mozilla.social.core.ui.common

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

fun shareUrl(url: String, context: Context) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }

    ContextCompat.startActivity(
        context,
        Intent.createChooser(sendIntent, null),
        null
    )
}