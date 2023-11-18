package org.mozilla.social.common.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@RequiresOptIn(
    message = "A use case exists for this method that should probably be used instead.",
    level = RequiresOptIn.Level.WARNING,
)
annotation class PreferUseCase
