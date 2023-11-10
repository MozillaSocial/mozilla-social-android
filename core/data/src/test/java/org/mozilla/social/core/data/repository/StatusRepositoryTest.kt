package org.mozilla.social.core.data.repository

import kotlin.test.BeforeTest

class StatusRepositoryTest : BaseRepositoryTest() {

    private lateinit var subject: StatusRepository

    @BeforeTest
    fun setup() {
        subject = StatusRepository(
            statusApi = statusApi,
            socialDatabase = socialDatabase,
        )
    }
}