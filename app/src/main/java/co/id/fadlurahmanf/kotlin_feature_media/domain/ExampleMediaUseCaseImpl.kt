package co.id.fadlurahmanf.kotlin_feature_media.domain

import android.content.Context
import com.github.fadlurahmanfdev.kotlin_feature_media.data.model.MediaAlbumModel
import com.github.fadlurahmanfdev.kotlin_feature_media.data.model.MediaItemModel
import com.github.fadlurahmanfdev.kotlin_feature_media.data.repositories.MediaRepository

class ExampleMediaUseCaseImpl(
    private val mediaRepository: MediaRepository
) : ExampleMediaUseCase {
    override fun getPhotoAlbums(context: Context): List<MediaAlbumModel> {
        return mediaRepository.getPhotoAlbums(context)
    }

    override fun getVideoAlbums(context: Context): List<MediaAlbumModel> {
        return mediaRepository.getVideoAlbums(context)
    }

    override fun getPhotos(context: Context, albumId: Long?): List<MediaItemModel> {
        return if (albumId != null) {
            mediaRepository.getPhotos(context, albumId = albumId)
        } else {
            mediaRepository.getPhotos(context)
        }
    }

    override fun getVideos(context: Context, albumId: Long?): List<MediaItemModel> {
        return if (albumId != null) {
            mediaRepository.getVideos(context, albumId = albumId)
        } else {
            mediaRepository.getVideos(context)
        }
    }

}