package org.mozilla.social.core.analytics.core

import android.content.Context

internal interface Analytics {
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
        recommendationId: String? = null,
        uiAdditionalDetail: String? = null,
        uiIdentifier: String? = null,
    ) = Unit

    /**
     * Event triggered when a user views a notable UI element.
     * Triggered once per page load, as soon as any pixel of that UI
     * element is visible in the foreground for any length of time.
     * UI elements may include: content, pages, CTAs, etc.
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
        recommendationId: String? = null,
        uiAdditionalDetail: String? = null,
        uiIdentifier: String? = null,
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
     * Event triggered when a user moves the mobile app to foreground by starting the app or
     * returning from the home screen/another app.
     */
    fun appOpened()

    /**
     * Event triggered when a user moves the mobile app to background by opening another app,
     * returning to the home screen, or quitting the app.
     */
    fun appBackgrounded()
}
