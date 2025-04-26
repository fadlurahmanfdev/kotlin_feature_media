package com.fadlurahmanfdev.media_grab.data.model

/**
 * Model class for bucket/album.
 *
 * @param id bucket/album id
 * @param name bucket/album name
 * @param thumbnail most recently item in album
 * @param itemCount total item inside media grab
 * */
data class MediaGrabAlbumModel(
    val id: Long,
    val name: String,
    val thumbnail: Thumbnail?,
    val itemCount: Int?,
) {
    /**
     * Model class for item from an album
     *
     * @param id thumbnail item id
     * @param path thumbnail item path
     * @param name thumbnail item name
     * @param type thumbnail item type (e.g., images/jpeg, videos/mp4)
     * @param dateAdded timestamp when the thumbnail item added into bucket/album
     * */
    data class Thumbnail(
        val id: Long,
        val path: String,
        val name: String,
        val type: String?,
        val dateAdded: Long?,
    )
}