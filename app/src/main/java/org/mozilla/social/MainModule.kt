package org.mozilla.social

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.navigation.SplashViewModel

val mainModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { SplashViewModel(get()) }
}