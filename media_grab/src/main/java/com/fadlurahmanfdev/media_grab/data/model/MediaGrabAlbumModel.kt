package com.fadlurahmanfdev.media_grab.data.model

data class MediaGrabAlbumModel(
    val id: Long,
    val name: String,
    val itemCount: Int?,
    val thumbnail: Thumbnail?,
) {
    data class Thumbnail(
        val id: Long,
        val path: String,
        val name: String,
    )
}