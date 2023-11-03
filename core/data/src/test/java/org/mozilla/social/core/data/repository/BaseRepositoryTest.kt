package org.mozilla.social.core.data.repository

import io.mockk.every
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk
import org.mozilla.social.core.data.utils.TransactionUtils
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.dao.AccountTimelineStatusDao
import org.mozilla.social.core.database.dao.AccountsDao
import org.mozilla.social.core.database.dao.FederatedTimelineStatusDao
import org.mozilla.social.core.database.dao.HashTagTimelineStatusDao
import org.mozilla.social.core.database.dao.HashtagDao
import org.mozilla.social.core.database.dao.HomeTimelineStatusDao
import org.mozilla.social.core.database.dao.LocalTimelineStatusDao
import org.mozilla.social.core.database.dao.PollsDao
import org.mozilla.social.core.database.dao.RelationshipsDao
import org.mozilla.social.core.database.dao.StatusDao
import org.mozilla.social.core.network.AccountApi
import org.mozilla.social.core.network.AppApi
import org.mozilla.social.core.network.InstanceApi
import org.mozilla.social.core.network.MediaApi
import org.mozilla.social.core.network.OauthApi
import org.mozilla.social.core.network.RecommendationApi
import org.mozilla.social.core.network.ReportApi
import org.mozilla.social.core.network.SearchApi
import org.mozilla.social.core.network.StatusApi
import org.mozilla.social.core.network.TimelineApi
import kotlin.test.BeforeTest

open class BaseRepositoryTest {

    @SpyK protected val accountApi = mockk<AccountApi>(relaxed = true)
    @SpyK protected val appApi = mockk<AppApi>(relaxed = true)
    @SpyK protected val instanceApi = mockk<InstanceApi>(relaxed = true)
    @SpyK protected val mediaApi = mockk<MediaApi>(relaxed = true)
    @SpyK protected val oauthApi = mockk<OauthApi>(relaxed = true)
    @SpyK protected val recommendationApi = mockk<RecommendationApi>(relaxed = true)
    @SpyK protected val reportApi = mockk<ReportApi>(relaxed = true)
    @SpyK protected val searchApi = mockk<SearchApi>(relaxed = true)
    @SpyK protected val statusApi = mockk<StatusApi>(relaxed = true)
    @SpyK protected val timelineApi = mockk<TimelineApi>(relaxed = true)

    @SpyK protected val socialDatabase = mockk<SocialDatabase>(relaxed = true)

    @SpyK protected val accountsDao = mockk<AccountsDao>(relaxed = true)
    @SpyK protected val accountTimelineDao = mockk<AccountTimelineStatusDao>(relaxed = true)
    @SpyK protected val federatedTimelineDao = mockk<FederatedTimelineStatusDao>(relaxed = true)
    @SpyK protected val hashtagDao = mockk<HashtagDao>(relaxed = true)
    @SpyK protected val hashTagTimelineDao = mockk<HashTagTimelineStatusDao>(relaxed = true)
    @SpyK protected val homeTimelineDao = mockk<HomeTimelineStatusDao>(relaxed = true)
    @SpyK protected val localTimelineDao = mockk<LocalTimelineStatusDao>(relaxed = true)
    @SpyK protected val pollsDao = mockk<PollsDao>(relaxed = true)
    @SpyK protected val relationshipsDao = mockk<RelationshipsDao>(relaxed = true)
    @SpyK protected val statusDao = mockk<StatusDao>(relaxed = true)

    @BeforeTest
    fun setupBaseTest() {
        every { socialDatabase.accountsDao() } returns accountsDao
        every { socialDatabase.accountTimelineDao() } returns accountTimelineDao
        every { socialDatabase.federatedTimelineDao() } returns federatedTimelineDao
        every { socialDatabase.hashtagDao() } returns hashtagDao
        every { socialDatabase.hashTagTimelineDao() } returns hashTagTimelineDao
        every { socialDatabase.homeTimelineDao() } returns homeTimelineDao
        every { socialDatabase.localTimelineDao() } returns localTimelineDao
        every { socialDatabase.pollDao() } returns pollsDao
        every { socialDatabase.relationshipsDao() } returns relationshipsDao
        every { socialDatabase.statusDao() } returns statusDao

        TransactionUtils.setupTransactionMock(socialDatabase)
    }
}