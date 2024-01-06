package org.mozilla.social.core.workmanager

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.BlocksRepository
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.FavoritesRepository
import org.mozilla.social.core.repository.mastodon.FollowersRepository
import org.mozilla.social.core.repository.mastodon.FollowingsRepository
import org.mozilla.social.core.repository.mastodon.HashtagRepository
import org.mozilla.social.core.repository.mastodon.MutesRepository
import org.mozilla.social.core.repository.mastodon.PollRepository
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.repository.mastodon.SearchRepository
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DatabasePurgeWorker(
    context: Context,
    workerParams: WorkerParameters,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    private val databaseDelegate: DatabaseDelegate,
    private val accountRepository: AccountRepository,
    private val blocksRepository: BlocksRepository,
    private val favoritesRepository: FavoritesRepository,
    private val followersRepository: FollowersRepository,
    private val followingsRepository: FollowingsRepository,
    private val hashtagRepository: HashtagRepository,
    private val mutesRepository: MutesRepository,
    private val pollRepository: PollRepository,
    private val relationshipRepository: RelationshipRepository,
    private val searchRepository: SearchRepository,
    private val statusRepository: StatusRepository,
    private val timelineRepository: TimelineRepository,
): CoroutineWorker(
    context,
    workerParams,
) {

    private val loggedInUserId = getLoggedInUserAccountId()

    override suspend fun doWork(): Result {
        return try {
            databaseDelegate.withTransaction {
                blocksRepository.deleteAll()
                favoritesRepository.deleteFavoritesTimeline()
                followersRepository.deleteAllFollowers()
                followingsRepository.deleteAllFollowings()
                mutesRepository.deleteAll()
                searchRepository.deleteSearchResults()
                timelineRepository.deleteLocalTimeline()
                timelineRepository.deleteFederatedTimeline()
                timelineRepository.deleteAllHashTagTimelines()
                timelineRepository.deleteAllAccountTimelines()

                hashtagRepository.deleteAll()
                relationshipRepository.deleteAll()

                val homeStatuses = timelineRepository.getTopHomePostsFromDatabase(40)
                val statusIdsToKeep = buildList {
                    addAll(homeStatuses.map { it.statusId })
                    addAll(homeStatuses.mapNotNull { it.boostedStatus?.statusId })
                }
                val accountIdsToKeep = buildList {
                    addAll(homeStatuses.map { it.account.accountId })
                    addAll(homeStatuses.mapNotNull { it.boostedStatus?.account?.accountId })
                    add(loggedInUserId)
                }
                val pollsIdsToKeep = buildList {
                    addAll(homeStatuses.mapNotNull { it.poll?.pollId })
                    addAll(homeStatuses.mapNotNull { it.boostedStatus?.poll?.pollId })
                }

                statusRepository.deleteAllLocal(statusIdsToKeep)
                accountRepository.deleteAllLocal(accountIdsToKeep)
                pollRepository.deleteAll(pollsIdsToKeep)
            }

            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure()
        }
    }

    companion object {
        const val WORKER_NAME = "purge"
        const val TEST_WORKER_NAME = "purgeTest"
    }
}

fun setupPurgeWork(
    context: Activity,
    coroutineScope: LifecycleCoroutineScope,
) {
    val workRequest = PeriodicWorkRequestBuilder<DatabasePurgeWorker>(7, TimeUnit.DAYS)
        .setConstraints(
            Constraints.Builder()
                .setRequiresDeviceIdle(true)
                .build()
        )
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        DatabasePurgeWorker.WORKER_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest,
    )

    coroutineScope.launch {
        WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow(DatabasePurgeWorker.WORKER_NAME)
            .collect {
                val workInfo = it.first()
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    context.finish()
                }
            }
    }
}

fun testPurge(
    context: Activity,
    coroutineScope: LifecycleCoroutineScope,
) {
    val workRequest = OneTimeWorkRequest.from(DatabasePurgeWorker::class.java)

    WorkManager.getInstance(context).beginUniqueWork(
        DatabasePurgeWorker.TEST_WORKER_NAME,
        ExistingWorkPolicy.KEEP,
        workRequest,
    ).enqueue()

    coroutineScope.launch {
        WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow(DatabasePurgeWorker.TEST_WORKER_NAME)
            .collect {
                val workInfo = it.first()
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    context.finish()
                }
            }
    }
}