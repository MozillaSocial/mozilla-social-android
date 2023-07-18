package org.mozilla.social.model

data class Page<T>(
    val contents: T,
    val nextPage: PageInfo? = null,
    val previousPage: PageInfo? = null
)