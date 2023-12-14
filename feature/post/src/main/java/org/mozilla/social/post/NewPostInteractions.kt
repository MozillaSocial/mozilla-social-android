package org.mozilla.social.post

interface NewPostInteractions {
    fun onScreenViewed() = Unit

    fun onUploadImageClicked() = Unit

    fun onUploadMediaClicked() = Unit
}
