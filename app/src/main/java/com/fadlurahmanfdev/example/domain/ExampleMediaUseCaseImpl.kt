package com.fadlurahmanfdev.example.domain

import android.content.Context
import com.fadlurahmanfdev.pixmed.data.model.PixMedBucket
import com.fadlurahmanfdev.pixmed.data.model.PixMedItem
import com.fadlurahmanfdev.pixmed.data.repositories.PixMedRepository

class ExampleMediaUseCaseImpl(
    private val mediaRepository: PixMedRepository,
) : ExampleMediaUseCase {
    override fun getAlbums(context: Context): List<PixMedBucket> {
        return mediaRepository.getAlbums(context)
    }

    override fun getPhotoAlbums(context: Context): List<PixMedBucket> {
        return mediaRepository.getPhotoAlbums(context)
    }

    override fun getVideoAlbums(context: Context): List<PixMedBucket> {
        return mediaRepository.getVideoAlbums(context)
    }

    override fun getPhotos(context: Context, albumId: Long?): List<PixMedItem> {
        return if (albumId != null) {
            mediaRepository.getPhotos(context, cursorProvider = null).items ?: listOf()
        } else {
            mediaRepository.getPhotos(context, cursorProvider = null).items ?: listOf()
        }
    }

    override fun getVideos(context: Context, albumId: Long?): List<PixMedItem> {
        return if (albumId != null) {
            mediaRepository.getVideos(context, cursorProvider = null).items ?: listOf()
        } else {
            mediaRepository.getVideos(context, cursorProvider = null).items ?: listOf()
        }
    }

}