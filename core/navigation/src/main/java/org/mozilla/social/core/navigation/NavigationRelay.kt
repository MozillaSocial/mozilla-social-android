package org.mozilla.social.core.navigation

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class NavigationRelay<ND : INavDestination> {

    private val _navigationEvents = MutableSharedFlow<ND>(replay = 1)
    val navigationEvents: SharedFlow<ND>
        get() = _navigationEvents

    fun emitNavEvent(navDestination: ND) {
        println("navrelay emit $navDestination")
        GlobalScope.launch {
            _navigationEvents.emit(navDestination)
        }
    }
}

