package co.id.fadlurahmanf.kotlin_feature_media.domain

import android.content.Context
import com.fadlurahmanfdev.media_grab.data.model.MediaAlbumModel
import com.fadlurahmanfdev.media_grab.data.model.MediaItemModel

interface ExampleMediaUseCase {
    fun getAlbums(context: Context): List<MediaAlbumModel>
    fun getPhotoAlbums(context: Context): List<MediaAlbumModel>
    fun getVideoAlbums(context: Context): List<MediaAlbumModel>
    fun getPhotos(context: Context, albumId: Long?): List<MediaItemModel>
    fun getVideos(context: Context, albumId: Long?): List<MediaItemModel>
}