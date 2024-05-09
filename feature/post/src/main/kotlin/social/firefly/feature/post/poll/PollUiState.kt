package social.firefly.feature.post.poll

data class PollUiState(
    val options: List<String>,
    val style: PollStyle,
    val pollDuration: PollDuration,
    val hideTotals: Boolean,
)
