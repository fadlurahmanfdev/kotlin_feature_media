package com.fadlurahmanfdev.media_grab.data.model

open class MediaGrabItemModel(
    open val id: Long?=null,
    open val path: String,
    val displayName: String,
    val bucketId: Long? = null,
    val bucketName: String? = null,
    val dateAdded: Long? = null,
    val dateModified: Long? = null,
    val dateTaken: Long? = null,
    val resolution: String? = null,
)