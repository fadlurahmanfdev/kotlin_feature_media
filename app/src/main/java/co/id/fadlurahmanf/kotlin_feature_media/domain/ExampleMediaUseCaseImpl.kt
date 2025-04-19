package co.id.fadlurahmanf.kotlin_feature_media.domain

import android.content.Context
import com.fadlurahmanfdev.media_grab.data.model.MediaAlbumModel
import com.fadlurahmanfdev.media_grab.data.model.MediaItemModel
import com.fadlurahmanfdev.media_grab.data.repositories.MediaRepository

class ExampleMediaUseCaseImpl(
    private val mediaRepository: MediaRepository
) : ExampleMediaUseCase {
    override fun getAlbums(context: Context): List<MediaAlbumModel> {
        return mediaRepository.getAlbums(context)
    }

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