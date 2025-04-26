package com.fadlurahmanfdev.media_grab.data.model

/**
 * Model class for picker list of media item.
 *
 * @param size total size of one time fetch
 * @param offset index where the cursor start from
 * @param nextOffset index where the next cursor should start from,
 * if there is no result anymore, the [nextOffset] will be null
 * @param totalItems total item of all media item, not in one time fetch
 * @param items list of media item
 * */
data class MediaGrabPickerResultModel(
    val size: Int,
    val offset: Int,
    val nextOffset: Int? = null,
    val totalItems: Int,
    val items: List<MediaGrabItemModel>? = null,
)
