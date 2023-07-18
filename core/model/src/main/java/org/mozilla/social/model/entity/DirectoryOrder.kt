package org.mozilla.social.model.entity

enum class DirectoryOrder(public val value: String) {

    /**
     * Sort by most recently posted statuses.
     */
    Active("active"),

    /**
     * Sort by most recently created accounts.
     */
    New("new")
}
