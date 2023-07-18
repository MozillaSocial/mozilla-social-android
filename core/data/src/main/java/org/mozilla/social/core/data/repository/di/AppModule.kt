package org.mozilla.social.core.data.repository.di

import org.koin.dsl.module
import org.mozilla.social.core.data.repository.UserRepository
import org.mozilla.social.core.data.repository.UserRepositoryImpl

val appModule = module {
    single<UserRepository> { UserRepositoryImpl() }
}