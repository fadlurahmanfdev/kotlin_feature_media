package com.fadlurahmanfdev.example.domain

import android.content.Context
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabAlbumModel
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabItemModel

interface ExampleMediaUseCase {
    fun getAlbums(context: Context): List<MediaGrabAlbumModel>
    fun getPhotoAlbums(context: Context): List<MediaGrabAlbumModel>
    fun getVideoAlbums(context: Context): List<MediaGrabAlbumModel>
    fun getPhotos(context: Context, albumId: Long?): List<MediaGrabItemModel>
    fun getVideos(context: Context, albumId: Long?): List<MediaGrabItemModel>
}