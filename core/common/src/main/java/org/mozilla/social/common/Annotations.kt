package org.mozilla.social.common

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@RequiresOptIn(
    message = "A use case exists for this method that should generally be used instead.",
    level = RequiresOptIn.Level.WARNING
)
annotation class PreferUseCase(
    val message: String
)