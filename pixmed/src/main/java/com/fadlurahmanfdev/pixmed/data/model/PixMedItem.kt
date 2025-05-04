package com.fadlurahmanfdev.pixmed.data.model

/**
 * Model class for media item (image or video)
 *
 * @param id item media id
 * @param path item media path
 * @param name item media display name
 * @param type item media type (e.g., images/jpeg, videos/mp4)
 * @param duration duration of the video, if the type is image, duration will be null
 * @param dateAdded the date when the item is added into file/photo/album
 * @param dateModified the date when the item is lastly modified
 * @param resolution the resolution of media item
 * */
data class PixMedItem(
    val id: Long?=null,
    val path: String,
    val name: String,
    val type: String? = null,
    val duration: Long? = null,
    val dateAdded: Long? = null,
    val dateModified: Long? = null,
    val resolution: String? = null,
    val bucket: Bucket? = null,
) {
    /**
     * Model class for bucket/album of a media item
     *
     * @param id bucket/album id
     * @param name bucket/album display name
     * */
    data class Bucket(
        val id: Long? = null,
        val name: String? = null,
    )
}