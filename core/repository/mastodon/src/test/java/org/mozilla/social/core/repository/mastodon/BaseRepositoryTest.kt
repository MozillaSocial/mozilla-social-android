package org.mozilla.social.core.repository.mastodon

import io.mockk.every
import io.mockk.mockk
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.dao.AccountTimelineStatusDao
import org.mozilla.social.core.database.dao.AccountsDao
import org.mozilla.social.core.database.dao.FederatedTimelineStatusDao
import org.mozilla.social.core.database.dao.HashTagTimelineStatusDao
import org.mozilla.social.core.database.dao.HashtagDao
import org.mozilla.social.core.database.dao.HomeTimelineStatusDao
import org.mozilla.social.core.database.dao.LocalTimelineStatusDao
import org.mozilla.social.core.database.dao.RelationshipsDao
import org.mozilla.social.core.database.dao.StatusDao
import org.mozilla.social.core.database.datasource.PollLocalDataSource
import org.mozilla.social.core.network.mastodon.AccountApi
import org.mozilla.social.core.network.mastodon.AppApi
import org.mozilla.social.core.network.mastodon.InstanceApi
import org.mozilla.social.core.network.mastodon.MediaApi
import org.mozilla.social.core.network.mastodon.OauthApi
import org.mozilla.social.core.network.mastodon.ReportApi
import org.mozilla.social.core.network.mastodon.SearchApi
import org.mozilla.social.core.network.mastodon.StatusApi
import org.mozilla.social.core.network.mastodon.TimelineApi
import kotlin.test.BeforeTest

open class BaseRepositoryTest {

    protected val accountApi = mockk<AccountApi>(relaxed = true)
    protected val appApi = mockk<AppApi>(relaxed = true)
    protected val instanceApi = mockk<InstanceApi>(relaxed = true)
    protected val mediaApi = mockk<MediaApi>(relaxed = true)
    protected val oauthApi = mockk<OauthApi>(relaxed = true)
    protected val reportApi = mockk<ReportApi>(relaxed = true)
    protected val searchApi = mockk<SearchApi>(relaxed = true)
    protected val statusApi = mockk<StatusApi>(relaxed = true)
    protected val timelineApi = mockk<TimelineApi>(relaxed = true)

    protected val socialDatabase = mockk<SocialDatabase>(relaxed = true)
    protected val pollLocalDataSource = mockk<PollLocalDataSource>(relaxed = true)

    protected val accountsDao = mockk<AccountsDao>(relaxed = true)
    protected val accountTimelineDao = mockk<AccountTimelineStatusDao>(relaxed = true)
    protected val federatedTimelineDao = mockk<FederatedTimelineStatusDao>(relaxed = true)
    protected val hashtagDao = mockk<HashtagDao>(relaxed = true)
    protected val hashTagTimelineDao = mockk<HashTagTimelineStatusDao>(relaxed = true)
    protected val homeTimelineDao = mockk<HomeTimelineStatusDao>(relaxed = true)
    protected val localTimelineDao = mockk<LocalTimelineStatusDao>(relaxed = true)
    protected val relationshipsDao = mockk<RelationshipsDao>(relaxed = true)
    protected val statusDao = mockk<StatusDao>(relaxed = true)

    @BeforeTest
    fun setupBaseTest() {
        every { socialDatabase.accountsDao() } returns accountsDao
        every { socialDatabase.accountTimelineDao() } returns accountTimelineDao
        every { socialDatabase.federatedTimelineDao() } returns federatedTimelineDao
        every { socialDatabase.hashtagDao() } returns hashtagDao
        every { socialDatabase.hashTagTimelineDao() } returns hashTagTimelineDao
        every { socialDatabase.homeTimelineDao() } returns homeTimelineDao
        every { socialDatabase.localTimelineDao() } returns localTimelineDao
        every { socialDatabase.relationshipsDao() } returns relationshipsDao
        every { socialDatabase.statusDao() } returns statusDao

        TransactionUtils.setupTransactionMock(socialDatabase)
    }
}