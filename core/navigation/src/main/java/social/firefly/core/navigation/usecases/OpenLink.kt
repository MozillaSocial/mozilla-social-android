package social.firefly.core.navigation.usecases

import social.firefly.core.navigation.Event
import social.firefly.core.navigation.EventRelay

class OpenLink(
    private val eventRelay: EventRelay,
) {
    operator fun invoke(url: String) {
        eventRelay.emitEvent(Event.OpenLink(url))
    }
}
