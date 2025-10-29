package com.fadlurahmanfdev.pixmed.data.repositories

import android.content.Context
import android.database.Cursor
import com.fadlurahmanfdev.pixmed.PixMed
import com.fadlurahmanfdev.pixmed.data.model.PixMedBucket
import com.fadlurahmanfdev.pixmed.data.model.PixMedPickerResult

/**
 * Repository class for implementation of MediaGrab
 *
 * Check [PixMed] for implementation
 * */
interface PixMedRepository {
    /**
     * Get list all of photos
     * */
    fun getPhotos(context: Context, cursorProvider: ((Context) -> Cursor?)?): PixMedPickerResult

    /**
     * Get list all of photos with pagination.
     *
     * @param offset index cursor should start from
     * @param size total item count should be return in one fetch
     * */
    fun getPhotos(
        context: Context,
        cursorProvider: ((Context) -> Cursor?)?,
        offset: Int = 0,
        size: Int = 20,
    ): PixMedPickerResult

    /**
     * Get list all of videos
     * */
    fun getVideos(context: Context, cursorProvider: ((Context) -> Cursor?)?): PixMedPickerResult

    /**
     * Get list all of videos with pagination.
     *
     * @param offset index cursor should start from
     * @param size total item count should be return in one fetch
     * */
    fun getVideos(
        context: Context,
        cursorProvider: ((Context) -> Cursor?)?,
        offset: Int = 0,
        size: Int = 20,
    ): PixMedPickerResult

    /**
     * Get list all of photo albums
     *
     * */
    fun getPhotoAlbums(context: Context): List<PixMedBucket>

    /**
     * Get list all of video albums
     *
     * */
    fun getVideoAlbums(context: Context): List<PixMedBucket>

    /**
     * Get list all of album
     *
     * */
    fun getAlbums(context: Context): List<PixMedBucket>
}