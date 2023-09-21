package org.mozilla.social

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.navigation.SplashViewModel
import org.mozilla.social.ui.navigationdrawer.NavigationDrawerViewModel

val mainModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { parametersHolder ->
        NavigationDrawerViewModel(
            get(),
            parametersHolder[0],
        )
    }
}