package org.mozilla.social.core.workmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.launch
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.FollowersRepository
import org.mozilla.social.core.repository.mastodon.FollowingsRepository
import org.mozilla.social.core.repository.mastodon.HashtagRepository
import org.mozilla.social.core.repository.mastodon.NotificationsRepository
import org.mozilla.social.core.repository.mastodon.PollRepository
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import timber.log.Timber
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * A worker that purges most data from the database.
 */
class DatabasePurgeWorker(
    context: Context,
    workerParams: WorkerParameters,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    private val databaseDelegate: DatabaseDelegate,
    private val accountRepository: AccountRepository,
    private val followersRepository: FollowersRepository,
    private val followingsRepository: FollowingsRepository,
    private val hashtagRepository: HashtagRepository,
    private val pollRepository: PollRepository,
    private val relationshipRepository: RelationshipRepository,
    private val statusRepository: StatusRepository,
    private val timelineRepository: TimelineRepository,
    private val notificationsRepository: NotificationsRepository,
): CoroutineWorker(
    context,
    workerParams,
) {

    private val loggedInUserId = getLoggedInUserAccountId()

    override suspend fun doWork(): Result {
        return try {
            databaseDelegate.withTransaction {
                followersRepository.deleteAllFollowers(listOf(loggedInUserId))
                followingsRepository.deleteAllFollowings(listOf(loggedInUserId))
                timelineRepository.deleteAllHashTagTimelines()
                timelineRepository.deleteAllAccountTimelines(listOf(loggedInUserId))

                // order matters because some queries will look at other entities to determine
                // what can be deleted
                notificationsRepository.deleteOldNotifications()
                // statuses must be after notifications
                statusRepository.deleteOldStatusesFromDatabase()
                // accounts must be after statuses and notifications
                accountRepository.deleteOldAccountFromDatabase(listOf(loggedInUserId))
                // relationships must be after accounts
                relationshipRepository.deleteOldRelationships()
                // polls must be after statuses
                pollRepository.deleteOldPolls()
                hashtagRepository.deleteAll()

                databaseDelegate.vacuum()
            }

            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure()
        }
    }

    companion object {
        private const val WORKER_NAME = "purge"
        private const val TEST_WORKER_NAME = "purgeTest"

        /**
         * Creates a periodic work request for the [DatabasePurgeWorker]
         * It then observes that work and restarts the app when complete.
         * We need to restart the app if it is open because purging the database could put us in
         * a weird state.
         * The user won't be using the app during this because there is a constraint put on the work request.
         */
        fun setupPurgeWork(
            activity: Activity,
            lifecycleCoroutineScope: LifecycleCoroutineScope,
        ) {
            val workRequest = PeriodicWorkRequestBuilder<DatabasePurgeWorker>(7, TimeUnit.DAYS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresDeviceIdle(true)
                        .build()
                )
                .build()

            WorkManager.getInstance(activity).enqueueUniquePeriodicWork(
                WORKER_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest,
            )

            observeWork(
                activity,
                lifecycleCoroutineScope,
                WORKER_NAME
            )
        }

        /**
         * Can be used for UI testing in the future, or manual testing now.
         */
        fun setupTestPurgeWork(
            activity: Activity,
            lifecycleCoroutineScope: LifecycleCoroutineScope,
            delay: Duration = Duration.ofSeconds(0),
        ) {
            val workRequest = OneTimeWorkRequestBuilder<DatabasePurgeWorker>()
                .setInitialDelay(delay)
                .build()

            WorkManager.getInstance(activity).beginUniqueWork(
                TEST_WORKER_NAME,
                ExistingWorkPolicy.KEEP,
                workRequest,
            ).enqueue()

            observeWork(
                activity,
                lifecycleCoroutineScope,
                TEST_WORKER_NAME
            )
        }

        /**
         * Observes a work request and restarted that app when the request is complete.
         * Because the coroutine scope is a [LifecycleCoroutineScope], the app will not restart
         * if it is not running.
         */
        private fun observeWork(
            activity: Activity,
            lifecycleCoroutineScope: LifecycleCoroutineScope,
            workName: String,
        ) {
            lifecycleCoroutineScope.launch {
                WorkManager.getInstance(activity)
                    .getWorkInfosForUniqueWorkFlow(workName)
                    .collect {
                        val workInfo = it.first()
                        if (workInfo.state == WorkInfo.State.SUCCEEDED || workInfo.state == WorkInfo.State.CANCELLED) {
                            activity.startActivity(Intent.makeRestartActivityTask(activity.intent.component))
                        }
                    }
            }
        }

    }
}
