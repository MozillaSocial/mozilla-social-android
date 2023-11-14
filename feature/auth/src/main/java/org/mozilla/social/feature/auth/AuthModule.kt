package org.mozilla.social.feature.auth

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.feature.auth.chooseServer.ChooseServerViewModel
import org.mozilla.social.feature.auth.login.LoginViewModel

val authModule = module {
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { ChooseServerViewModel(get()) }
}