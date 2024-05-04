package com.github.fadlurahmanfdev.kotlin_feature_media.data.model

import com.github.fadlurahmanfdev.kotlin_feature_media.data.enum.MediaItemType

data class MediaItemModel(
    val id: Long,
    val path: String,
    val type: MediaItemType,
    val bucketId: Long,
    val bucketName: String,
    val dateAdded: Long,
)