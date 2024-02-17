package social.firefly.post

import social.firefly.core.model.StatusVisibility

interface NewPostInteractions {
    fun onScreenViewed()
    fun onUploadImageClicked()
    fun onUploadMediaClicked()
    fun onPostClicked()
    fun onEditClicked()
    fun onVisibilitySelected(statusVisibility: StatusVisibility)
}

object NewPostInteractionsNoOp : social.firefly.post.NewPostInteractions {
    override fun onScreenViewed() = Unit
    override fun onUploadImageClicked() = Unit
    override fun onUploadMediaClicked() = Unit
    override fun onPostClicked() = Unit
    override fun onEditClicked() = Unit
    override fun onVisibilitySelected(statusVisibility: StatusVisibility) = Unit
}
