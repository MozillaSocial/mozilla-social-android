package org.mozilla.social.di.module

import org.koin.dsl.module
import org.mozilla.social.di.data.UserRepository
import org.mozilla.social.di.data.UserRepositoryImpl

val appModule = module {
    single<UserRepository> { UserRepositoryImpl() }
}