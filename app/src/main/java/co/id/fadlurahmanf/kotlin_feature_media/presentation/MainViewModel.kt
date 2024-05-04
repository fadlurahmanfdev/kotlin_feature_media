package co.id.fadlurahmanf.kotlin_feature_media.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import co.id.fadlurahmanf.kotlin_feature_media.domain.ExampleMediaUseCase
import com.github.fadlurahmanfdev.kotlin_feature_media.data.model.MediaAlbumModel
import com.github.fadlurahmanfdev.kotlin_feature_media.data.model.MediaItemModel

class MainViewModel(
    private val exampleMediaUseCase: ExampleMediaUseCase
) : ViewModel() {
    fun getAlbums(context: Context): List<MediaAlbumModel> {
        return exampleMediaUseCase.getAlbums(context)
    }

    fun getPhotoAlbums(context: Context): List<MediaAlbumModel> {
        return exampleMediaUseCase.getPhotoAlbums(context)
    }

    fun getVideoAlbums(context: Context): List<MediaAlbumModel> {
        return exampleMediaUseCase.getVideoAlbums(context)
    }

    fun getPhotos(context: Context, albumId: Long?): List<MediaItemModel> {
        return exampleMediaUseCase.getPhotos(context, albumId = albumId)
    }

    fun getVideos(context: Context, albumId: Long?): List<MediaItemModel> {
        return exampleMediaUseCase.getVideos(context, albumId = albumId)
    }
}