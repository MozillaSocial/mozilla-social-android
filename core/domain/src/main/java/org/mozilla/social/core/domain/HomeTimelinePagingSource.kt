package org.mozilla.social.core.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.mozilla.social.model.Post

class HomeTimelinePagingSource(
    private val homeTimelineListHolder: HomeTimelineListHolder,
) : PagingSource<String, Post>() {

    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        println("johnny paging source refresh")
        // refresh key is null so when a refresh happens we always grab the current
        // list from the list holder.
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        return try {
            println("johnny paging source : key = ${params.key}")

            // if the list is empty or the key is null, just return the list holder's list
            val response = if (homeTimelineListHolder.timeline.isEmpty() || params.key == null) {
                homeTimelineListHolder.timeline
            } else {
                // otherwise we need to
                val index = homeTimelineListHolder.timeline.indexOfFirst { it.status.id == params.key }
                val lastIndex = if (homeTimelineListHolder.timeline.size > index + 20) {
                    index + 20
                } else {
                    homeTimelineListHolder.timeline.lastIndex
                }
                buildList { addAll(homeTimelineListHolder.timeline) }.subList(index, lastIndex)
            }
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = response.lastOrNull()?.status?.id
            )
        } catch (e: Exception) {
            println("johnny paging source error $e")
            LoadResult.Error(e)
        }
    }
}