package org.mozilla.social.model.entity

enum class SearchType(public val value: String) {

    /**
     * Search for accounts.
     */
    Accounts("accounts"),

    /**
     * Search for hashtags.
     */
    Hashtags("hashtags"),

    /**
     * Search for statuses.
     */
    Statuses("statuses")
}
