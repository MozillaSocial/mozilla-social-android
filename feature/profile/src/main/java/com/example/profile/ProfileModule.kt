package com.example.profile

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ProfileModule = module {
    viewModel { parameters -> ProfileViewModel(get()) }
}