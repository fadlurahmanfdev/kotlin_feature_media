package co.id.fadlurahmanf.kotlin_feature_media.domain

import android.content.Context
import com.github.fadlurahmanfdev.kotlin_feature_media.data.model.MediaAlbumModel
import com.github.fadlurahmanfdev.kotlin_feature_media.data.model.MediaItemModel

interface ExampleMediaUseCase {
    fun getAlbums(context: Context): List<MediaAlbumModel>
    fun getPhotoAlbums(context: Context): List<MediaAlbumModel>
    fun getVideoAlbums(context: Context): List<MediaAlbumModel>
    fun getPhotos(context: Context, albumId: Long?): List<MediaItemModel>
    fun getVideos(context: Context, albumId: Long?): List<MediaItemModel>
}