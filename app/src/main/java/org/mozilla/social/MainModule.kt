package org.mozilla.social

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.navigation.SplashViewModel
import org.mozilla.social.ui.NavigationViewModel
import org.mozilla.social.ui.PostCardNavigationImpl

val mainModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { NavigationViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get()) }
    single<PostCardNavigation> { PostCardNavigationImpl(get()) }
}