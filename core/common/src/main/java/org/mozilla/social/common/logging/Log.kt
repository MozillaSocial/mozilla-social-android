package org.mozilla.social.common.logging

interface Log {
    fun v(message: String)
    fun d(message: String)
    fun i(message: String)
    fun w(message: String)
    fun w(throwable: Throwable)
    fun e(throwable: Throwable)
    fun wtf(throwable: Throwable)
}