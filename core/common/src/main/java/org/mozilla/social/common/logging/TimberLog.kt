package org.mozilla.social.common.logging

import timber.log.Timber

class TimberLog(
    isDebug: Boolean
) : Log {

    init {
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun v(message: String) {
        Timber.v(message)
    }

    override fun d(message: String) {
        Timber.d(message)
    }

    override fun i(message: String) {
        Timber.i(message)
    }

    override fun w(message: String) {
        Timber.w(message)
    }

    override fun w(throwable: Throwable) {
        Timber.w(throwable)
    }

    override fun e(throwable: Throwable) {
        Timber.e(throwable)
    }

    override fun wtf(throwable: Throwable) {
        Timber.wtf(throwable)
    }
}