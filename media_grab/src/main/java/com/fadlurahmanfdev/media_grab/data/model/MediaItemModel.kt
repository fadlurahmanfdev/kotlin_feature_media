package com.fadlurahmanfdev.media_grab.data.model

import com.fadlurahmanfdev.media_grab.data.enum.MediaItemType

data class MediaItemModel(
    val id: Long,
    val path: String,
    val type: MediaItemType,
    val bucketId: Long,
    val bucketName: String,
    val dateAdded: Long,
)