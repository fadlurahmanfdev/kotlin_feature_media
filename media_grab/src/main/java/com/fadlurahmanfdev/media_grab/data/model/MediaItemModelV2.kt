package com.fadlurahmanfdev.media_grab.data.model

import com.fadlurahmanfdev.media_grab.data.enum.MediaItemType

data class MediaItemModelV2(
    val id: Long? = null,
    val path: String? = null,
    val type: MediaItemType? = null,
    val bucketId: Long? = null,
    val bucketName: String? = null,
    val dateAdded: Long? = null,
    val dateModified: Long? = null,
    val dateTaken: Long? = null,
    val resolution: String? = null,
)