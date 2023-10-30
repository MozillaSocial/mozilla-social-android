package org.mozilla.social.core.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface NavigationRelay<T> {

    val navigationEvents: Flow<T>
    fun emitNavEvent(t: T)
}

class MoSoNavigationRelay : NavigationRelay<NavDestination> {

    private val _navigationEvents = MutableSharedFlow<NavDestination>(replay = 1)
    override val navigationEvents: SharedFlow<NavDestination>
        get() = _navigationEvents

    override fun emitNavEvent(navDestination: NavDestination) {
        println("navrelay emit $navDestination")
        GlobalScope.launch {
            _navigationEvents.emit(navDestination)
        }
    }
}

sealed class NavDestination {
    data object Login : NavDestination()
    data class NewPost(val replyId: String?) : NavDestination()
    data class Thread(val statusId: String) : NavDestination()

    data class Report(val accountId: String, val accountHandle: String, val statusId: String) :
        NavDestination()

    data object Feed : NavDestination()
}