package com.fadlurahmanfdev.example.domain

import android.content.Context
import com.fadlurahmanfdev.pixmed.data.model.PixMedBucket
import com.fadlurahmanfdev.pixmed.data.model.PixMedItem

interface ExampleMediaUseCase {
    fun getAlbums(context: Context): List<PixMedBucket>
    fun getPhotoAlbums(context: Context): List<PixMedBucket>
    fun getVideoAlbums(context: Context): List<PixMedBucket>
    fun getPhotos(context: Context, albumId: Long?): List<PixMedItem>
    fun getVideos(context: Context, albumId: Long?): List<PixMedItem>
}