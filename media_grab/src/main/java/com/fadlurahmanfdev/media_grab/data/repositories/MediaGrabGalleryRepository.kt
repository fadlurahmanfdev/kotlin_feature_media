package com.fadlurahmanfdev.media_grab.data.repositories

import android.content.Context
import com.fadlurahmanfdev.media_grab.data.model.MediaAlbumModel
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabItemModel

interface MediaGrabGalleryRepository {
    fun getPhotos(context: Context): List<MediaGrabItemModel>
    fun getAlbums(context: Context): List<MediaAlbumModel>
}