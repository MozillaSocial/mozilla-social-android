package org.mozilla.social.feature.account

interface OverflowInteractions {
    fun onOverflowMuteClicked(accountId: String) = Unit
    fun onOverflowBlockClicked(accountId: String) = Unit
    fun onOverflowReportClicked(accountId: String) = Unit
}