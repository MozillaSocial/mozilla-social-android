package org.mozilla.social.common

/**
 * Only the main app module has access to the version code and name, so we set
 * those values in the application's onCreate here for use in other screens
 */
object Version {
    var name: String = "0.0.0"
    var code: Int = 0
}