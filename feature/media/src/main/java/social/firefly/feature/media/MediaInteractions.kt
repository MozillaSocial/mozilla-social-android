package social.firefly.feature.media

interface MediaInteractions {
    fun onDownloadClicked(fileName: String)
}

object MediaInteractionsNoOp : MediaInteractions {
    override fun onDownloadClicked(fileName: String) = Unit
}