package org.mozilla.social.core.navigation.usecases

import org.mozilla.social.core.navigation.Event
import org.mozilla.social.core.navigation.EventRelay

class OpenLink(
    private val eventRelay: EventRelay,
) {
    operator fun invoke(url: String) {
        eventRelay.emitEvent(Event.OpenLink(url))
    }
}
