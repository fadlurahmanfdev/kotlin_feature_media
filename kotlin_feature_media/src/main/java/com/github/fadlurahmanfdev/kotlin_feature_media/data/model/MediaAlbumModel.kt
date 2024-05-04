package com.github.fadlurahmanfdev.kotlin_feature_media.data.model

import com.github.fadlurahmanfdev.kotlin_feature_media.data.enum.MediaItemType

data class MediaAlbumModel(
    val id: Long,
    val name: String,
    val thumbnailPath: String,
    val thumbnailPathDateAdded: Long,
    val thumbnailPathType: MediaItemType,
    val itemCount: Int,
)