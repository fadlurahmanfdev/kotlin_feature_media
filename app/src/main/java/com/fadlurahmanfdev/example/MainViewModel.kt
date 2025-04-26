package com.fadlurahmanfdev.example

import android.content.Context
import androidx.lifecycle.ViewModel
import com.fadlurahmanfdev.example.domain.ExampleMediaUseCase
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabAlbumModel
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabItemModel

class MainViewModel(
    private val exampleMediaUseCase: ExampleMediaUseCase
) : ViewModel() {
    fun getAlbums(context: Context): List<MediaGrabAlbumModel> {
        return exampleMediaUseCase.getAlbums(context)
    }

    fun getPhotoAlbums(context: Context): List<MediaGrabAlbumModel> {
        return exampleMediaUseCase.getPhotoAlbums(context)
    }

    fun getVideoAlbums(context: Context): List<MediaGrabAlbumModel> {
        return exampleMediaUseCase.getVideoAlbums(context)
    }

    fun getPhotos(context: Context, albumId: Long?): List<MediaGrabItemModel> {
        return exampleMediaUseCase.getPhotos(context, albumId = albumId)
    }

    fun getVideos(context: Context, albumId: Long?): List<MediaGrabItemModel> {
        return exampleMediaUseCase.getVideos(context, albumId = albumId)
    }
}