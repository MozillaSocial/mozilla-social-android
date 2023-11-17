package org.mozilla.social.post.poll

data class Poll(
    val options: List<String>,
    val style: PollStyle,
    val pollDuration: PollDuration,
    val hideTotals: Boolean,
)
