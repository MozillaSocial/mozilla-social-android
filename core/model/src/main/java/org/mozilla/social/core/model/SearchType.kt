package org.mozilla.social.core.model

enum class SearchType(val value: String) {
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
    Statuses("statuses"),
}
