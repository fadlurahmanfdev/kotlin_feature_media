package com.fadlurahmanfdev.media_grab.data.repositories

import android.content.Context
import com.fadlurahmanfdev.media_grab.MediaGrab
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabAlbumModel
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabPickerResultModel

/**
 * Repository class for implementation of MediaGrab
 *
 * Check [MediaGrab] for implementation
 * */
interface MediaGrabRepository {
    /**
     * Get list all of photos
     * */
    fun getPhotos(context: Context): MediaGrabPickerResultModel

    /**
     * Get list all of photos with pagination.
     *
     * @param offset index cursor should start from
     * @param size total item count should be return in one fetch
     * */
    fun getPhotos(
        context: Context,
        offset: Int = 0,
        size: Int = 20,
    ): MediaGrabPickerResultModel

    /**
     * Get list all of videos
     * */
    fun getVideos(context: Context): MediaGrabPickerResultModel

    /**
     * Get list all of videos with pagination.
     *
     * @param offset index cursor should start from
     * @param size total item count should be return in one fetch
     * */
    fun getVideos(
        context: Context,
        offset: Int = 0,
        size: Int = 20,
    ): MediaGrabPickerResultModel

    /**
     * Get list all of photo albums
     *
     * */
    fun getPhotoAlbums(context: Context): List<MediaGrabAlbumModel>

    /**
     * Get list all of video albums
     *
     * */
    fun getVideoAlbums(context: Context): List<MediaGrabAlbumModel>

    /**
     * Get list all of album
     *
     * */
    fun getAlbums(context: Context): List<MediaGrabAlbumModel>
}