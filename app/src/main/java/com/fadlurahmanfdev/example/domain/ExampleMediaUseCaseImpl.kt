package com.fadlurahmanfdev.example.domain

import android.content.Context
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabAlbumModel
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabItemModel
import com.fadlurahmanfdev.media_grab.data.repositories.MediaGrabRepository

class ExampleMediaUseCaseImpl(
    private val mediaRepository: MediaGrabRepository
) : ExampleMediaUseCase {
    override fun getAlbums(context: Context): List<MediaGrabAlbumModel> {
        return mediaRepository.getAlbums(context)
    }

    override fun getPhotoAlbums(context: Context): List<MediaGrabAlbumModel> {
        return mediaRepository.getPhotoAlbums(context)
    }

    override fun getVideoAlbums(context: Context): List<MediaGrabAlbumModel> {
        return mediaRepository.getVideoAlbums(context)
    }

    override fun getPhotos(context: Context, albumId: Long?): List<MediaGrabItemModel> {
        return if (albumId != null) {
            mediaRepository.getPhotos(context).items ?: listOf()
        } else {
            mediaRepository.getPhotos(context).items ?: listOf()
        }
    }

    override fun getVideos(context: Context, albumId: Long?): List<MediaGrabItemModel> {
        return if (albumId != null) {
            mediaRepository.getVideos(context).items ?: listOf()
        } else {
            mediaRepository.getVideos(context).items ?: listOf()
        }
    }

}