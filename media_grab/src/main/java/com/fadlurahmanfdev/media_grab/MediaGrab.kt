package com.fadlurahmanfdev.media_grab

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.util.Log
import androidx.core.content.ContextCompat
import com.fadlurahmanfdev.media_grab.constant.MediaGrabExceptionConstant
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabAlbumModel
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabItemModel
import com.fadlurahmanfdev.media_grab.data.model.MediaGrabPickerResultModel
import com.fadlurahmanfdev.media_grab.data.repositories.MediaGrabRepository
import com.fadlurahmanfdev.media_grab.exception.MediaGrabException
import com.fadlurahmanfdev.media_grab.other.BaseMediaGrab

class MediaGrab : BaseMediaGrab(), MediaGrabRepository {
    /**
     * Get list of all photos in gallery.
     *
     * @throws MediaGrabExceptionConstant.READ_MEDIA_PERMISSION_NOT_GRANTED permission not granted
     * */
    override fun getPhotos(context: Context): MediaGrabPickerResultModel {
        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw MediaGrabExceptionConstant.READ_MEDIA_PERMISSION_NOT_GRANTED
            }
            val photos = getMediaItems(context, ::getPhotoCursor, ::getMediaGrabPhoto)
            return MediaGrabPickerResultModel(
                size = photos.size,
                offset = 0,
                nextOffset = null,
                totalItems = photos.size,
                items = photos,
            )
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "MediaGrab-LOG %%% failed get photos", t)
            if (t is MediaGrabException) {
                throw t
            }
            throw MediaGrabExceptionConstant.UNEXPECTED.copy(message = t.message)
        }
    }

    /**
     * Get list of photos in gallery using pagination.
     *
     * @param offset where should index start from.
     * @param size total item in one fetch/request.
     *
     * @throws MediaGrabExceptionConstant.READ_MEDIA_PERMISSION_NOT_GRANTED permission not granted
     * */
    override fun getPhotos(
        context: Context,
        offset: Int,
        size: Int,
    ): MediaGrabPickerResultModel {
        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw MediaGrabExceptionConstant.READ_MEDIA_PERMISSION_NOT_GRANTED
            }
            return getMediaItems(context, offset, size, ::getPhotoCursor, ::getMediaGrabPhoto)
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "MediaGrab-LOG %%% failed get photos", t)
            if (t is MediaGrabException) {
                throw t
            }
            throw MediaGrabExceptionConstant.UNEXPECTED.copy(message = t.message)
        }
    }

    /**
     * Get list of all videos in gallery.
     *
     * @throws MediaGrabExceptionConstant.READ_MEDIA_PERMISSION_NOT_GRANTED permission not granted
     * */
    override fun getVideos(context: Context): MediaGrabPickerResultModel {
        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_MEDIA_VIDEO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw MediaGrabExceptionConstant.READ_MEDIA_PERMISSION_NOT_GRANTED
            }
            val videos = getMediaItems(context, ::getVideoCursor, ::getMediaGrabVideo)
            return MediaGrabPickerResultModel(
                size = videos.size,
                offset = 0,
                nextOffset = null,
                totalItems = videos.size,
                items = videos
            )
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "MediaGrab-LOG %%% failed get videos", t)
            if (t is MediaGrabException) {
                throw t
            }
            throw MediaGrabExceptionConstant.UNEXPECTED.copy(message = t.message)
        }
    }

    /**
     * Get list of videos in gallery using pagination.
     *
     * @param offset where should index start from.
     * @param size total item in one fetch/request.
     *
     * @throws MediaGrabExceptionConstant.READ_MEDIA_PERMISSION_NOT_GRANTED permission not granted
     * */
    override fun getVideos(
        context: Context,
        offset: Int,
        size: Int,
    ): MediaGrabPickerResultModel {
        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_MEDIA_VIDEO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw MediaGrabExceptionConstant.READ_MEDIA_PERMISSION_NOT_GRANTED
            }
            return getMediaItems(context, offset, size, ::getVideoCursor, ::getMediaGrabVideo)
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "MediaGrab-LOG %%% failed get videos", t)
            if (t is MediaGrabException) {
                throw t
            }
            throw MediaGrabExceptionConstant.UNEXPECTED.copy(message = t.message)
        }
    }

    private fun getMediaItems(
        context: Context,
        cursorProvider: (Context) -> Cursor?,
        mapper: (Cursor) -> MediaGrabItemModel,
    ): List<MediaGrabItemModel> {
        val items = arrayListOf<MediaGrabItemModel>()
        var cursor: Cursor? = null
        try {
            cursor = cursorProvider(context)
                ?: throw MediaGrabExceptionConstant.CANNOT_INITIALIZED_CURSOR

            while (cursor.moveToNext()) {
                items.add(mapper(cursor))
            }
            return items
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "MediaGrab-LOG %%% failed get media item", t)
            if (t is MediaGrabException) {
                throw t
            }
            throw MediaGrabExceptionConstant.UNEXPECTED.copy(message = t.message)
        } finally {
            cursor?.close()
        }
    }

    private fun getMediaItems(
        context: Context,
        offset: Int,
        size: Int,
        cursorProvider: (Context) -> Cursor?,
        mapper: (Cursor) -> MediaGrabItemModel,
    ): MediaGrabPickerResultModel {
        val items = arrayListOf<MediaGrabItemModel>()
        var cursor: Cursor? = null

        try {
            cursor = cursorProvider(context)
                ?: throw MediaGrabExceptionConstant.CANNOT_INITIALIZED_CURSOR

            val itemCount = cursor.count

            if (offset > itemCount) throw MediaGrabExceptionConstant.INVALID_OFFSET

            if (offset > 0) {
                cursor.moveToPosition(offset)
            }

            if (itemCount <= size) {
                while (cursor.moveToNext()) {
                    items.add(mapper(cursor))
                }
                return MediaGrabPickerResultModel(
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

            return MediaGrabPickerResultModel(
                size = size,
                offset = offset,
                items = items,
                nextOffset = nextOffset,
                totalItems = itemCount,
            )

        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "MediaGrab-LOG %%% failed get media items", t)
            if (t is MediaGrabException) {
                throw t
            }
            throw MediaGrabExceptionConstant.UNEXPECTED.copy(message = t.message)
        } finally {
            cursor?.close()
        }
    }

    /**
     * Get list of all photo albums in gallery.
     *
     * */
    override fun getPhotoAlbums(context: Context): List<MediaGrabAlbumModel> {
        val photoResult: MediaGrabPickerResultModel = getPhotos(context)
        val photoItems: List<MediaGrabItemModel> = photoResult.items ?: listOf()
        return mapperAlbums(context) { photoItems }
    }

    /**
     * Get list of all video albums in gallery.
     *
     * */
    override fun getVideoAlbums(context: Context): List<MediaGrabAlbumModel> {
        val videoResult: MediaGrabPickerResultModel = getVideos(context)
        val videoItems: List<MediaGrabItemModel> = videoResult.items ?: listOf()
        return mapperAlbums(context) { videoItems }
    }

    /**
     * Get list of all albums in gallery.
     *
     * */
    override fun getAlbums(context: Context): List<MediaGrabAlbumModel> {
        try {
            val photoAlbums: List<MediaGrabAlbumModel> = getPhotoAlbums(context)
            val videoAlbums: List<MediaGrabAlbumModel> = getVideoAlbums(context)
            val combined = (photoAlbums + videoAlbums)

            val mergedAlbums = combined
                .groupBy { it.id }
                .map { (_, group) ->
                    val mostRecent = group.maxByOrNull { it.thumbnail?.dateAdded ?: 0L }
                    MediaGrabAlbumModel(
                        id = group.first().id,
                        name = group.first().name,
                        thumbnail = mostRecent?.thumbnail,
                        itemCount = group.map { it.itemCount ?: 0 }.toList().sumOf { it }
                    )
                }

            return mergedAlbums
        } catch (t: Throwable) {
            throw MediaGrabExceptionConstant.UNEXPECTED.copy(message = t.message)
        }
    }

    private fun mapperAlbums(
        context: Context,
        itemsProvider: (Context) -> List<MediaGrabItemModel>,
    ): List<MediaGrabAlbumModel> {
        try {
            val albums = arrayListOf<MediaGrabAlbumModel>()
            val items = itemsProvider(context)

            for (element in items) {
                if (element.bucket?.id == null) continue
                if (isAlbumExist(element.bucket.id, albums)) continue

                val thumbnailItem = items.firstOrNull {
                    it.bucket?.id == element.bucket.id
                }
                var thumbnail: MediaGrabAlbumModel.Thumbnail? = null
                if (thumbnailItem != null) {
                    thumbnail = MediaGrabAlbumModel.Thumbnail(
                        id = thumbnailItem.id,
                        path = thumbnailItem.path,
                        name = thumbnailItem.name,
                        dateAdded = thumbnailItem.dateAdded,
                        type = thumbnailItem.type,
                    )
                }
                albums.add(
                    MediaGrabAlbumModel(
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
            Log.e(this::class.java.simpleName, "MediaGrab-LOG %%% failed get albums", t)
            throw MediaGrabExceptionConstant.UNEXPECTED.copy(message = t.message)
        }
    }

    private fun isAlbumExist(bucketId: Long, albums: List<MediaGrabAlbumModel>): Boolean =
        albums.firstOrNull {
            it.id == bucketId
        } != null

    private fun getMediaGrabPhoto(cursor: Cursor): MediaGrabItemModel {
        return MediaGrabItemModel(
            id = getId(cursor) ?: throw MediaGrabExceptionConstant.UNABLE_GET_ID,
            name = getDisplayName(cursor)
                ?: throw MediaGrabExceptionConstant.UNABLE_GET_DISPLAY_NAME,
            path = getPath(cursor)
                ?: throw MediaGrabExceptionConstant.UNABLE_GET_REAL_PATH,
            type = getMimeType(cursor),
            dateAdded = getDateAdded(cursor),
            dateModified = getDateModified(cursor),
            resolution = getResolution(cursor),
            bucket = MediaGrabItemModel.Bucket(
                id = getBucketId(cursor),
                name = getBucketName(cursor),
            )
        )
    }

    private fun getMediaGrabVideo(cursor: Cursor): MediaGrabItemModel {
        return MediaGrabItemModel(
            id = getId(cursor) ?: throw MediaGrabExceptionConstant.UNABLE_GET_ID,
            name = getDisplayName(cursor)
                ?: throw MediaGrabExceptionConstant.UNABLE_GET_DISPLAY_NAME,
            path = getPath(cursor)
                ?: throw MediaGrabExceptionConstant.UNABLE_GET_REAL_PATH,
            type = getMimeType(cursor),
            duration = getDuration(cursor),
            dateAdded = getDateAdded(cursor),
            dateModified = getDateModified(cursor),
            resolution = getResolution(cursor),
            bucket = MediaGrabItemModel.Bucket(
                id = getBucketId(cursor),
                name = getBucketName(cursor),
            )
        )
    }
}