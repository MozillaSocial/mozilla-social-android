package org.mozilla.social.post.poll

data class Poll(
    val options: List<String>,
    val multipleChoice: Boolean,
    val pollDuration: PollDuration,
    val hideTotals: Boolean,
)