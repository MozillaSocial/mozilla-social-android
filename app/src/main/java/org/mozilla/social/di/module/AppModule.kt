package org.mozilla.social.di.module

import org.koin.dsl.module
import org.mozilla.social.repository.UserRepository
import org.mozilla.social.repository.UserRepositoryImpl

val appModule = module {
    single<UserRepository> { UserRepositoryImpl() }
}