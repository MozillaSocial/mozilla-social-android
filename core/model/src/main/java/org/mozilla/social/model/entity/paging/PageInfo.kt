package org.mozilla.social.model.entity.paging

/**
 * Represents a pagination state, to get a specific page of results from an API.
 *
 * Considering the latest status ids:
 *
 * > 1, 2, 3 … 98, 99, 100
 *
 * Then these queries will result in:
 *
 * | `min_id` | `since_id` | `max_id` | `limit` | result              |
 * |----------|------------|----------|---------|---------------------|
 * | 1        |            |          | 5       | 2, 3, 4, 5, 6       |
 * |          | 1          |          | 5       | 96, 97, 98, 99, 100 |
 * |          | 97         |          |         | 98, 99, 100         |
 * |          | 50         | 54       |         | 51, 52, 53          |
 */
data class PageInfo(
    /**
     * If set, the API will only include items *after* this ID,
     * but not necessarily starting at this ID.
     *
     * Non-inclusive.
     */
    val sinceId: String? = null,

    /**
     * If set, the API will not return items before this ID (not inclusive).
     * The page will therefore start just after the item with this ID.
     */
    val minId: String? = null,

    /**
     * If set, the API will not return items after this ID (not inclusive).
     * The page will therefore end just before the item with this ID.
     */
    val maxId: String? = null
)
