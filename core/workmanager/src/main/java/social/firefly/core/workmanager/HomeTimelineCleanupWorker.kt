package social.firefly.core.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import social.firefly.core.repository.mastodon.TimelineRepository
import timber.log.Timber

class HomeTimelineCleanupWorker(
    context: Context,
    private val workerParams: WorkerParameters,
    private val timelineRepository: TimelineRepository,
) : CoroutineWorker(
    context,
    workerParams,
) {

    override suspend fun doWork(): Result {
        println("johnny work started")
        return try {
            workerParams.inputData.getString(LAST_SEEN_ID)?.let {
                println("worker id $it")
                timelineRepository.deleteHomeStatusesBeforeId(it)
            }
            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure()
        }
    }

    companion object {
        private const val LAST_SEEN_ID = "lastSeenId"
        private const val WORKER_NAME = "homeTimelineCleanup"

        var lastStatusViewedId: String = ""

        fun setupWorker(
            context: Context,
        ) {
            if (lastStatusViewedId.isNotBlank()) {
                val workRequest = OneTimeWorkRequestBuilder<HomeTimelineCleanupWorker>()
                    .setInputData(
                        Data.Builder()
                            .putString(LAST_SEEN_ID, lastStatusViewedId)
                            .build()
                    )
                    .build()

                WorkManager.getInstance(context).enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.KEEP,
                    workRequest,
                )
            }
        }
    }
}