package com.fadlurahmanfdev.media_grab.data.model

data class MediaGrabVideoModel(
    override var id: Long,
    val type: String? = null,
) : MediaGrabItemModel(
    id = id,
    displayName = "",
    path = "sa"
)