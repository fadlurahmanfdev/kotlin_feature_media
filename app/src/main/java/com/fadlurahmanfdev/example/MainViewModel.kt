package com.fadlurahmanfdev.example

import android.content.Context
import androidx.lifecycle.ViewModel
import com.fadlurahmanfdev.example.domain.ExampleMediaUseCase
import com.fadlurahmanfdev.pixmed.data.model.PixMedBucket
import com.fadlurahmanfdev.pixmed.data.model.PixMedItem

class MainViewModel(
    private val exampleMediaUseCase: ExampleMediaUseCase
) : ViewModel() {
    fun getAlbums(context: Context): List<PixMedBucket> {
        return exampleMediaUseCase.getAlbums(context)
    }

    fun getPhotoAlbums(context: Context): List<PixMedBucket> {
        return exampleMediaUseCase.getPhotoAlbums(context)
    }

    fun getVideoAlbums(context: Context): List<PixMedBucket> {
        return exampleMediaUseCase.getVideoAlbums(context)
    }

    fun getPhotos(context: Context, albumId: Long?): List<PixMedItem> {
        return exampleMediaUseCase.getPhotos(context, albumId = albumId)
    }

    fun getVideos(context: Context, albumId: Long?): List<PixMedItem> {
        return exampleMediaUseCase.getVideos(context, albumId = albumId)
    }
}