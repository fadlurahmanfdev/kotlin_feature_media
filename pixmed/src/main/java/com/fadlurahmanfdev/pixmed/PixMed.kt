package com.fadlurahmanfdev.pixmed

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.fadlurahmanfdev.pixmed.constant.PixMedExceptionConstant
import com.fadlurahmanfdev.pixmed.data.model.PixMedBucket
import com.fadlurahmanfdev.pixmed.data.model.PixMedItem
import com.fadlurahmanfdev.pixmed.data.model.PixMedPickerResult
import com.fadlurahmanfdev.pixmed.data.repositories.PixMedRepository
import com.fadlurahmanfdev.pixmed.exception.PixMedException
import com.fadlurahmanfdev.pixmed.other.BasePixMed

class PixMed : BasePixMed(), PixMedRepository {
    /**
     * Get list of all photos in gallery.
     *
     * @throws PixMedExceptionConstant.READ_MEDIA_IMAGES_PERMISSION_NOT_GRANTED permission not granted
     * */
    override fun getPhotos(
        context: Context,
        cursorProvider: ((Context) -> Cursor?)?,
    ): PixMedPickerResult {
        try {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.READ_MEDIA_IMAGES
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        throw PixMedExceptionConstant.READ_MEDIA_IMAGES_PERMISSION_NOT_GRANTED
                    }
                }

                else -> {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        throw PixMedExceptionConstant.READ_EXTERNAL_STORAGE_PERMISSION_NOT_GRANTED
                    }
                }
            }

            val photos =
                getMediaItems(context, cursorProvider ?: (::getPhotoCursor), ::getMediaGrabPhoto)
            return PixMedPickerResult(
                size = photos.size,
                offset = 0,
                nextOffset = null,
                totalItems = photos.size,
                items = photos,
            )
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "PixMedia-LOG %%% failed get photos", t)
            if (t is PixMedException) {
                throw t
            }
            throw PixMedExceptionConstant.UNEXPECTED.copy(message = t.message)
        }
    }

    /**
     * Get list of photos in gallery using pagination.
     *
     * @param offset where should index start from.
     * @param size total item in one fetch/request.
     *
     * @throws PixMedExceptionConstant.READ_MEDIA_IMAGES_PERMISSION_NOT_GRANTED permission not granted
     * */
    override fun getPhotos(
        context: Context,
        cursorProvider: ((Context) -> Cursor?)?,
        offset: Int,
        size: Int,
    ): PixMedPickerResult {
        try {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.READ_MEDIA_IMAGES
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        throw PixMedExceptionConstant.READ_MEDIA_IMAGES_PERMISSION_NOT_GRANTED
                    }
                }

                else -> {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        throw PixMedExceptionConstant.READ_EXTERNAL_STORAGE_PERMISSION_NOT_GRANTED
                    }
                }
            }
            return getMediaItems(
                context,
                offset,
                size,
                cursorProvider ?: (::getPhotoCursor),
                ::getMediaGrabPhoto
            )
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "PixMedia-LOG %%% failed get photos", t)
            if (t is PixMedException) {
                throw t
            }
            throw PixMedExceptionConstant.UNEXPECTED.copy(message = t.message)
        }
    }

    /**
     * Get list of all videos in gallery.
     *
     * @throws PixMedExceptionConstant.READ_MEDIA_IMAGES_PERMISSION_NOT_GRANTED permission not granted
     * */
    override fun getVideos(
        context: Context,
        cursorProvider: ((Context) -> Cursor?)?,
    ): PixMedPickerResult {
        try {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.READ_MEDIA_IMAGES
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        throw PixMedExceptionConstant.READ_MEDIA_IMAGES_PERMISSION_NOT_GRANTED
                    }
                }

                else -> {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        throw PixMedExceptionConstant.READ_EXTERNAL_STORAGE_PERMISSION_NOT_GRANTED
                    }
                }
            }
            val videos =
                getMediaItems(context, cursorProvider ?: (::getVideoCursor), ::getMediaGrabVideo)
            return PixMedPickerResult(
                size = videos.size,
                offset = 0,
                nextOffset = null,
                totalItems = videos.size,
                items = videos
            )
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "PixMedia-LOG %%% failed get videos", t)
            if (t is PixMedException) {
                throw t
            }
            throw PixMedExceptionConstant.UNEXPECTED.copy(message = t.message)
        }
    }

    /**
     * Get list of videos in gallery using pagination.
     *
     * @param offset where should index start from.
     * @param size total item in one fetch/request.
     *
     * @throws PixMedExceptionConstant.READ_MEDIA_IMAGES_PERMISSION_NOT_GRANTED permission not granted
     * */
    override fun getVideos(
        context: Context,
        cursorProvider: ((Context) -> Cursor?)?,
        offset: Int,
        size: Int,
    ): PixMedPickerResult {
        try {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.READ_MEDIA_IMAGES
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        throw PixMedExceptionConstant.READ_MEDIA_IMAGES_PERMISSION_NOT_GRANTED
                    }
                }

                else -> {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        throw PixMedExceptionConstant.READ_EXTERNAL_STORAGE_PERMISSION_NOT_GRANTED
                    }
                }
            }
            return getMediaItems(
                context,
                offset,
                size,
                cursorProvider ?: (::getVideoCursor),
                ::getMediaGrabVideo
            )
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "PixMedia-LOG %%% failed get videos", t)
            if (t is PixMedException) {
                throw t
            }
            throw PixMedExceptionConstant.UNEXPECTED.copy(message = t.message)
        }
    }

    private fun getMediaItems(
        context: Context,
        cursorProvider: (Context) -> Cursor?,
        mapper: (Cursor) -> PixMedItem,
    ): List<PixMedItem> {
        val items = arrayListOf<PixMedItem>()
        var cursor: Cursor? = null
        try {
            cursor = cursorProvider(context)
                ?: throw PixMedExceptionConstant.CANNOT_INITIALIZED_CURSOR

            while (cursor.moveToNext()) {
                items.add(mapper(cursor))
            }
            return items
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "PixMedia-LOG %%% failed get media item", t)
            if (t is PixMedException) {
                throw t
            }
            throw PixMedExceptionConstant.UNEXPECTED.copy(message = t.message)
        } finally {
            cursor?.close()
        }
    }

    private fun getMediaItems(
        context: Context,
        offset: Int,
        size: Int,
        cursorProvider: (Context) -> Cursor?,
        mapper: (Cursor) -> PixMedItem,
    ): PixMedPickerResult {
        val items = arrayListOf<PixMedItem>()
        var cursor: Cursor? = null

        try {
            cursor = cursorProvider(context)
                ?: throw PixMedExceptionConstant.CANNOT_INITIALIZED_CURSOR

            val itemCount = cursor.count

            if (offset > itemCount) throw PixMedExceptionConstant.INVALID_OFFSET

            if (offset > 0) {
                cursor.moveToPosition(offset)
            }

            if (itemCount <= size) {
                while (cursor.moveToNext()) {
                    items.add(mapper(cursor))
                }
                return PixMedPickerResult(
                    size = itemCount,
                    offset = offset,
                    items = items,
                    totalItems = itemCount,
                )
            }

            val lastCursorPositionShouldBe = offset + size
            var nextOffset: Int? = null

            while (cursor.moveToNext()) {
                items.add(mapper(cursor))
                val nextIndex = cursor.position + 1
                if (nextIndex >= lastCursorPositionShouldBe) break
            }

            if (!cursor.isLast) {
                nextOffset = (offset + size) - 1
            }

            return PixMedPickerResult(
                size = size,
                offset = offset,
                items = items,
                nextOffset = nextOffset,
                totalItems = itemCount,
            )

        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "PixMedia-LOG %%% failed get media items", t)
            if (t is PixMedException) {
                throw t
            }
            throw PixMedExceptionConstant.UNEXPECTED.copy(message = t.message)
        } finally {
            cursor?.close()
        }
    }

    /**
     * Get list of all photo albums in gallery.
     *
     * */
    override fun getPhotoAlbums(context: Context): List<PixMedBucket> {
        val photoResult: PixMedPickerResult = getPhotos(context, cursorProvider = null)
        val photoItems: List<PixMedItem> = photoResult.items ?: listOf()
        return mapperAlbums(context) { photoItems }
    }

    /**
     * Get list of all video albums in gallery.
     *
     * */
    override fun getVideoAlbums(context: Context): List<PixMedBucket> {
        val videoResult: PixMedPickerResult = getVideos(context, cursorProvider = null)
        val videoItems: List<PixMedItem> = videoResult.items ?: listOf()
        return mapperAlbums(context) { videoItems }
    }

    /**
     * Get list of all albums in gallery.
     *
     * */
    override fun getAlbums(context: Context): List<PixMedBucket> {
        try {
            val photoAlbums: List<PixMedBucket> = getPhotoAlbums(context)
            val videoAlbums: List<PixMedBucket> = getVideoAlbums(context)
            val combined = (photoAlbums + videoAlbums)

            val mergedAlbums = combined
                .groupBy { it.id }
                .map { (_, group) ->
                    val mostRecent = group.maxByOrNull { it.thumbnail?.dateAdded ?: 0L }
                    PixMedBucket(
                        id = group.first().id,
                        name = group.first().name,
                        thumbnail = mostRecent?.thumbnail,
                        itemCount = group.map { it.itemCount ?: 0 }.toList().sumOf { it }
                    )
                }

            return mergedAlbums
        } catch (t: Throwable) {
            throw PixMedExceptionConstant.UNEXPECTED.copy(message = t.message)
        }
    }

    private fun mapperAlbums(
        context: Context,
        itemsProvider: (Context) -> List<PixMedItem>,
    ): List<PixMedBucket> {
        try {
            val albums = arrayListOf<PixMedBucket>()
            val items = itemsProvider(context)

            for (element in items) {
                if (element.bucket?.id == null) continue
                if (isAlbumExist(element.bucket.id, albums)) continue

                val thumbnailItem = items.firstOrNull {
                    it.bucket?.id == element.bucket.id
                }
                var thumbnail: PixMedBucket.Thumbnail? = null
                if (thumbnailItem != null) {
                    thumbnail = PixMedBucket.Thumbnail(
                        id = thumbnailItem.id,
                        path = thumbnailItem.path,
                        name = thumbnailItem.name,
                        dateAdded = thumbnailItem.dateAdded,
                        type = thumbnailItem.type,
                    )
                }
                albums.add(
                    PixMedBucket(
                        id = element.bucket.id,
                        name = element.bucket.name ?: "",
                        itemCount = items.filter {
                            it.bucket?.id == element.bucket.id
                        }.size,
                        thumbnail = thumbnail,
                    )
                )
            }

            return albums
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "PixMedia-LOG %%% failed get albums", t)
            throw PixMedExceptionConstant.UNEXPECTED.copy(message = t.message)
        }
    }

    private fun isAlbumExist(bucketId: Long, albums: List<PixMedBucket>): Boolean =
        albums.firstOrNull {
            it.id == bucketId
        } != null

    private fun getMediaGrabPhoto(cursor: Cursor): PixMedItem {
        return PixMedItem(
            id = getId(cursor) ?: throw PixMedExceptionConstant.UNABLE_GET_ID,
            name = getDisplayName(cursor)
                ?: throw PixMedExceptionConstant.UNABLE_GET_DISPLAY_NAME,
            path = getPath(cursor)
                ?: throw PixMedExceptionConstant.UNABLE_GET_REAL_PATH,
            type = getMimeType(cursor),
            dateAdded = getDateAdded(cursor),
            dateModified = getDateModified(cursor),
            resolution = getResolution(cursor),
            bucket = PixMedItem.Bucket(
                id = getBucketId(cursor),
                name = getBucketName(cursor),
            )
        )
    }

    private fun getMediaGrabVideo(cursor: Cursor): PixMedItem {
        return PixMedItem(
            id = getId(cursor) ?: throw PixMedExceptionConstant.UNABLE_GET_ID,
            name = getDisplayName(cursor)
                ?: throw PixMedExceptionConstant.UNABLE_GET_DISPLAY_NAME,
            path = getPath(cursor)
                ?: throw PixMedExceptionConstant.UNABLE_GET_REAL_PATH,
            type = getMimeType(cursor),
            duration = getDuration(cursor),
            dateAdded = getDateAdded(cursor),
            dateModified = getDateModified(cursor),
            resolution = getResolution(cursor),
            bucket = PixMedItem.Bucket(
                id = getBucketId(cursor),
                name = getBucketName(cursor),
            )
        )
    }
}