package com.github.fadlurahmanfdev.kotlin_feature_media.data.repositories

import android.content.Context
import com.github.fadlurahmanfdev.kotlin_feature_media.data.model.MediaAlbumModel
import com.github.fadlurahmanfdev.kotlin_feature_media.data.model.MediaItemModel

interface MediaRepository {
    fun checkPermission(
        context: Context,
        onCompleteCheckPermission: (isImageGranted: Boolean, isVideoGranted: Boolean) -> Unit,
    )

    fun getAlbums(context: Context): List<MediaAlbumModel>
    fun getPhotoAlbums(context: Context): List<MediaAlbumModel>

    fun getVideoAlbums(context: Context): List<MediaAlbumModel>

    fun getPhotos(context: Context): List<MediaItemModel>

    fun getPhotos(context: Context, albumId: Long): List<MediaItemModel>

    fun getVideos(context: Context): List<MediaItemModel>

    fun getVideos(context: Context, albumId: Long): List<MediaItemModel>
}