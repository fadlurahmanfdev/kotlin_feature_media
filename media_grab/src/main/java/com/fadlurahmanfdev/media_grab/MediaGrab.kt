package com.fadlurahmanfdev.media_grab

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.fadlurahmanfdev.media_grab.constant.MediaGrabExceptionConstant
import com.fadlurahmanfdev.media_grab.data.model.MediaAlbumModel
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabItemModel
import com.fadlurahmanfdev.media_grab.data.repositories.MediaGrabGalleryRepository
import com.fadlurahmanfdev.media_grab.other.BaseMediaGrab

class MediaGrab : BaseMediaGrab(), MediaGrabGalleryRepository {
    override fun getPhotos(context: Context): List<MediaGrabItemModel> {
        val photos = arrayListOf<MediaGrabItemModel>()
        var cursor: Cursor? = null
        try {
            cursor = getPhotoCursor(context)
                ?: throw MediaGrabExceptionConstant.CANNOT_INITIALIZED_CURSOR

            Log.d(this::class.java.simpleName, "MediaGrab-LOG %%% total item: ${cursor.count}")

            while (cursor.moveToNext()) {
                photos.add(
                    MediaGrabItemModel(
                        id = getId(cursor) ?: throw MediaGrabExceptionConstant.UNABLE_GET_ID,
                        displayName = getDisplayName(cursor)
                            ?: throw MediaGrabExceptionConstant.UNABLE_GET_DISPLAY_NAME,
                        path = getPath(cursor)
                            ?: throw MediaGrabExceptionConstant.UNABLE_GET_REAL_PATH,
                        bucketId = getBucketId(cursor),
                        bucketName = getBucketName(cursor),
                        dateAdded = getDateAdded(cursor),
                        dateTaken = getDateTaken(cursor),
                        dateModified = getDateModified(cursor),
                        resolution = getResolution(cursor)
                    )
                )
            }
            return photos
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "MediaGrab-LOG %%% failed get photos", t)
            throw t
        } finally {
            cursor?.close()
        }
    }

    override fun getAlbums(context: Context): List<MediaAlbumModel> {
        try {
            val cursor = getPhotoCursor(context)
                ?: throw MediaGrabExceptionConstant.CANNOT_INITIALIZED_CURSOR
            return listOf()
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "MediaGrab-LOG %%% failed get albums", t)
            return listOf()
        }
    }
}