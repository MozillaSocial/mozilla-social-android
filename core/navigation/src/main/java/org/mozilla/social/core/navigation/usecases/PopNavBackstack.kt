package org.mozilla.social.core.navigation.usecases

import org.mozilla.social.core.navigation.Event
import org.mozilla.social.core.navigation.EventRelay

class PopNavBackstack(
    private val eventRelay: EventRelay,
) {
    operator fun invoke() {
        eventRelay.emitEvent(Event.PopBackStack)
    }
}
