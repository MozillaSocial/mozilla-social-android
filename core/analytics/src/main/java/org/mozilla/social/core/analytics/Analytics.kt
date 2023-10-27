package org.mozilla.social.core.analytics

import android.content.Context

interface Analytics {
    /**
     * Initialize the analytics SDK
     */
    fun initialize(context: Context) = Unit

    /**
     * Event triggered when a user taps/clicks on a UI element,
     * triggering a change in app state.
     *
     * @param engagementType The type of user engagement that triggered this event.
     * One of:
     * `follow`
     * `post`
     * `reply`
     * `favorite`
     * `boost`
     * `bookmark`
     * `general`
     *
     * @param engagementValue If the UI element is a toggle or setting, the value of the
     * toggle/setting after user interaction has taken place.
     *
     * @param mastodonAccountHandle The Mastodon account handle of the UI element that was seen/interacted
     * with by the user, if any.
     * For example, the account handle of the user who created a post that was
     * seen/interacted with. Or, the account handle of the user that was followed.
     *
     * @param mastodonAccountId The Mastodon account ID of the UI element that was seen/interacted
     * with by the user, if any.
     * For example, the account ID of the user who created a post that was
     * seen/interacted with. Or, the account ID of the user that was followed.
     *
     * @param mastodonStatusId The Mastodon status ID of the post that was seen/interacted
     * with by the user, if any.
     * The Mastodon API calls statuses "posts", but they are one and the same.
     *
     * @param recommendationId Recommendation identifier of the content
     * that was seen/interacted with by the user, if any.
     *
     * @param uiAdditionalDetail An optional string to record further informatin about the UI
     * element, such as its starting value if it is a toggle or setting.
     *
     * @param uiIdentifier The string id of the UI element that was seen/interacted
     * with by the user (e.g. `home.rec` or `home.boost`).
     * The identifier is a period-delimited string, ordered in
     * increasing order of specificity (e.g. `[SCREEN].[BUTTON]`).
     * All existing identifier values are being tracked here:
     * https://docs.google.com/spreadsheets/d/1KX6TiyXXg2fE0a1IDKsy5O97ZrHYvjKohmIX_m8ECXY/edit?usp=sharing
     */
    fun uiEngagement(
        engagementType: EngagementType? = null,
        engagementValue: String? = null,
        mastodonAccountHandle: String? = null,
        mastodonAccountId: String? = null,
        mastodonStatusId: String? = null,
        recommendationId: String? = null,
        uiAdditionalDetail: String? = null,
        uiIdentifier: String? = null
    ) = Unit

    /**
     * Event triggered when a user views a notable UI element.
     * Triggered once per page load, as soon as any pixel of that UI
     * element is visible in the foreground for any length of time.
     * UI elements may include: content, pages, CTAs, etc.
     *
     * @param mastodonAccountHandle The Mastodon account handle of the UI element that was seen/interacted
     * with by the user, if any.
     * For example, the account handle of the user who created a post that was
     * seen/interacted with. Or, the account handle of the user that was followed.
     *
     * @param mastodonAccountId The Mastodon account ID of the UI element that was seen/interacted
     * with by the user, if any.
     * For example, the account ID of the user who created a post that was
     * seen/interacted with. Or, the account ID of the user that was followed.
     *
     * @param mastodonStatusId The Mastodon status ID of the post that was seen/interacted
     * with by the user, if any.
     * The Mastodon API calls statuses "posts", but they are one and the same.
     *
     * @param recommendationId Recommendation identifier of the content
     * that was seen/interacted with by the user, if any.
     *
     * @param uiAdditionalDetail An optional string to record further informatin about the UI
     * element, such as its starting value if it is a toggle or setting.
     *
     * @param uiIdentifier The string id of the UI element that was seen/interacted
     * with by the user (e.g. `home.rec` or `home.boost`).
     * The identifier is a period-delimited string, ordered in
     * increasing order of specificity (e.g. `[SCREEN].[BUTTON]`).
     * All existing identifier values are being tracked here:
     * https://docs.google.com/spreadsheets/d/1KX6TiyXXg2fE0a1IDKsy5O97ZrHYvjKohmIX_m8ECXY/edit?usp=sharing
     */
    fun uiImpression(
        mastodonAccountHandle: String? = null,
        mastodonAccountId: String? = null,
        mastodonStatusId: String? = null,
        recommendationId: String? = null,
        uiAdditionalDetail: String? = null,
        uiIdentifier: String? = null
    ) = Unit

    /**
     * @param adjustDeviceId The Adjust device ID for this user
     */
    fun setAdjustDeviceId(adjustDeviceId: String) = Unit

    /**
     * @param fxaAccountId The user's FxA account ID, if available.
     */
    fun setFxaAccountId(fxaAccountId: String) = Unit

    /**
     * @param mastodonAccountHandle The user's full account handle, with domain.
     * For example: account_name@mozilla.social
     */
    fun setMastodonAccountHandle(mastodonAccountHandle: String) = Unit

    /**
     * @param mastodonAccountId The user's numeric account ID from Mastodon.
     */
    fun setMastodonAccountId(mastodonAccountId: String) = Unit

    /**
     * @param userAgent The device user agent string.
     */
    fun setUserAgent(userAgent: String) = Unit

    /**
     * Clear the set identifiers.  Call this on logout.
     */
    fun clearLoggedInIdentifiers() = Unit

    /**
     * @param toggleTracking Toggle user data tracking.
     */
    fun toggleAnalyticsTracking(toggleTracking: Boolean) = Unit
}
