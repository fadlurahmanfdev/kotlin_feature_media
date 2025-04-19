package com.fadlurahmanfdev.media_grab.data.model

import com.fadlurahmanfdev.media_grab.data.enum.MediaItemType

data class MediaAlbumModel(
    val id: Long,
    val name: String,
    val thumbnailPath: String,
    val thumbnailPathDateAdded: Long,
    val thumbnailPathType: MediaItemType,
    val itemCount: Int,
)