package com.github.fadlurahmanfdev.kotlin_feature_media.data.repositories

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat
import com.github.fadlurahmanfdev.kotlin_feature_media.data.enum.MediaItemType
import com.github.fadlurahmanfdev.kotlin_feature_media.data.model.MediaAlbumModel
import com.github.fadlurahmanfdev.kotlin_feature_media.data.model.MediaItemModel
import com.github.fadlurahmanfdev.kotlin_feature_media.other.BaseMedia
import java.util.Calendar

class MediaRepositoryImpl : BaseMedia(), MediaRepository {
    override fun checkPermission(
        context: Context,
        onCompleteCheckPermission: (isImageGranted: Boolean, isVideoGranted: Boolean) -> Unit,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val imageStatus = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_MEDIA_IMAGES
            )
            val videoStatus = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_MEDIA_VIDEO
            )
            onCompleteCheckPermission(
                imageStatus == PackageManager.PERMISSION_GRANTED,
                videoStatus == PackageManager.PERMISSION_GRANTED,
            )
        } else {
            val status = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission_group.STORAGE
            )
            onCompleteCheckPermission(
                status == PackageManager.PERMISSION_GRANTED,
                status == PackageManager.PERMISSION_GRANTED,
            )
        }
    }

    override fun getAlbums(context: Context): List<MediaAlbumModel> {
        try {
            val albums = arrayListOf<MediaAlbumModel>()
            val photoAlbums = getPhotoAlbums(context)
            val videoAlbums = getVideoAlbums(context)

            albums.addAll(photoAlbums)
            repeat(videoAlbums.size) { indexVideoAlbum ->
                if (albums.map { albumItem -> albumItem.id }.toList()
                        .contains(videoAlbums[indexVideoAlbum].id)
                ) {
                    val similarAlbum =
                        albums.first { albumItem -> albumItem.id == videoAlbums[indexVideoAlbum].id }
                    val oldIndex =
                        albums.indexOfFirst { albumItem -> albumItem.id == videoAlbums[indexVideoAlbum].id }
                    val oldItemCount = similarAlbum.itemCount
                    val newItemCount = videoAlbums[indexVideoAlbum].itemCount
                    val photoDate = Calendar.getInstance().apply {
                        timeInMillis = similarAlbum.thumbnailPathDateAdded
                    }
                    val videoDate = Calendar.getInstance().apply {
                        timeInMillis = videoAlbums[indexVideoAlbum].thumbnailPathDateAdded
                    }

                    albums[oldIndex] = similarAlbum.copy(
                        itemCount = oldItemCount + newItemCount,
                    )

                    if (videoDate.after(photoDate)) {
                        albums[oldIndex] = similarAlbum.copy(
                            thumbnailPath = videoAlbums[indexVideoAlbum].thumbnailPath,
                            thumbnailPathDateAdded = videoAlbums[indexVideoAlbum].thumbnailPathDateAdded,
                            thumbnailPathType = MediaItemType.VIDEO
                        )
                    }
                } else {
                    albums.add(videoAlbums[indexVideoAlbum])
                }
            }
            return albums.sortedWith(compareByDescending<MediaAlbumModel> {
                it.thumbnailPathDateAdded
            }.thenByDescending {
                it.thumbnailPathDateAdded
            })
        } catch (e: Throwable) {
            return listOf()
        }
    }

    override fun getPhotoAlbums(context: Context): List<MediaAlbumModel> {
        try {
            val cursor: Cursor? = getPhotoCursor(context)
            if (cursor == null) {
                Log.d(
                    MediaRepositoryImpl::class.java.simpleName,
                    "warn getAllPhotos: cursor is null"
                )
                return listOf()
            }

            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val bucketNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val buckedIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            val albums = arrayListOf<MediaAlbumModel>()
            val bucketIds = arrayListOf<Long>()
            while (cursor.moveToNext()) {
                val path = dataColumn.let { cursor.getString(it) }
                val bucket = bucketNameColumn.let { cursor.getString(it) }
                val bucketId = buckedIdColumn.let { cursor.getLong(it) }
                val date = dateAddedColumn.let { cursor.getLong(it) }

                if (bucketIds.contains(bucketId)) continue
                val bucketCursor: Cursor? = context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Images.Media.BUCKET_ID),
                    "${MediaStore.Images.Media.BUCKET_ID} = $bucketId",
                    null,
                    null,
                )
                val bucketCount = bucketCursor?.count
                bucketCursor?.close()

                albums.add(
                    MediaAlbumModel(
                        id = bucketId,
                        name = bucket,
                        thumbnailPath = path,
                        thumbnailPathDateAdded = date,
                        thumbnailPathType = MediaItemType.IMAGE,
                        itemCount = bucketCount ?: 0,
                    )
                )
                bucketIds.add(bucketId)
            }
            cursor.close()
            return albums
        } catch (e: Throwable) {
            Log.d(MediaRepositoryImpl::class.java.simpleName, "failed getPhotos: ${e.message}")
            return listOf()
        }
    }

    override fun getVideoAlbums(context: Context): List<MediaAlbumModel> {
        try {
            val cursor: Cursor? = getVideoCursor(context)
            if (cursor == null) {
                Log.d(
                    MediaRepositoryImpl::class.java.simpleName,
                    "warn getVideoAlbums: cursor is null"
                )
                return listOf()
            }

            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val bucketNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val buckedIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)

            val albums = arrayListOf<MediaAlbumModel>()
            val bucketIds = arrayListOf<Long>()
            while (cursor.moveToNext()) {
                val path = dataColumn.let { cursor.getString(it) }
                val bucket = bucketNameColumn.let { cursor.getString(it) }
                val bucketId = buckedIdColumn.let { cursor.getLong(it) }
                val date = dateAddedColumn.let { cursor.getLong(it) }

                if (bucketIds.contains(bucketId)) continue
                val bucketCursor: Cursor? = context.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Video.Media.BUCKET_ID),
                    "${MediaStore.Video.Media.BUCKET_ID} = $bucketId",
                    null,
                    null,
                )
                val bucketCount = bucketCursor?.count
                bucketCursor?.close()

                albums.add(
                    MediaAlbumModel(
                        id = bucketId,
                        name = bucket,
                        thumbnailPath = path,
                        thumbnailPathDateAdded = date,
                        thumbnailPathType = MediaItemType.VIDEO,
                        itemCount = bucketCount ?: 0,
                    )
                )
                bucketIds.add(bucketId)
            }
            cursor.close()
            return albums
        } catch (e: Throwable) {
            Log.d(MediaRepositoryImpl::class.java.simpleName, "failed getVideoAlbums: ${e.message}")
            return listOf()
        }
    }

    override fun getPhotos(context: Context): List<MediaItemModel> {
        try {
            val cursor: Cursor? = getPhotoCursor(context)
            if (cursor == null) {
                Log.d(
                    MediaRepositoryImpl::class.java.simpleName,
                    "warn getPhotos: cursor is null"
                )
                return listOf()
            }

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val bucketNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val buckedIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            val images = arrayListOf<MediaItemModel>()
            while (cursor.moveToNext()) {
                val id = idColumn.let { cursor.getLong(it) }
                val path = dataColumn.let { cursor.getString(it) }
                val bucket = bucketNameColumn.let { cursor.getString(it) }
                val bucketId = buckedIdColumn.let { cursor.getLong(it) }
                val date = dateAddedColumn.let { cursor.getLong(it) }

                images.add(
                    MediaItemModel(
                        id = id,
                        path = path,
                        type = MediaItemType.IMAGE,
                        bucketId = bucketId,
                        bucketName = bucket,
                        dateAdded = date,
                    )
                )
            }
            cursor.close()
            return images
        } catch (e: Throwable) {
            Log.d(MediaRepositoryImpl::class.java.simpleName, "failed getPhotos: ${e.message}")
            return listOf()
        }
    }

    override fun getPhotos(context: Context, albumId: Long): List<MediaItemModel> {
        try {
            val cursor: Cursor? = getPhotoCursor(
                context,
                selection = "${MediaStore.Images.Media.BUCKET_ID} = $albumId",
                selectionArgs = null
            )
            if (cursor == null) {
                Log.d(
                    MediaRepositoryImpl::class.java.simpleName,
                    "warn getPhotos: cursor is null"
                )
                return listOf()
            }

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val bucketNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val buckedIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            val images = arrayListOf<MediaItemModel>()
            while (cursor.moveToNext()) {
                val id = idColumn.let { cursor.getLong(it) }
                val path = dataColumn.let { cursor.getString(it) }
                val bucket = bucketNameColumn.let { cursor.getString(it) }
                val bucketId = buckedIdColumn.let { cursor.getLong(it) }
                val date = dateAddedColumn.let { cursor.getLong(it) }

                images.add(
                    MediaItemModel(
                        id = id,
                        path = path,
                        type = MediaItemType.IMAGE,
                        bucketId = bucketId,
                        bucketName = bucket,
                        dateAdded = date,
                    )
                )
            }
            cursor.close()
            return images
        } catch (e: Throwable) {
            Log.d(
                MediaRepositoryImpl::class.java.simpleName,
                "failed getPhotos, albumId: $albumId -> ${e.message}"
            )
            return listOf()
        }
    }

    override fun getVideos(context: Context): List<MediaItemModel> {
        try {
            val cursor: Cursor? = getVideoCursor(context)
            if (cursor == null) {
                Log.d(
                    MediaRepositoryImpl::class.java.simpleName,
                    "warn getVideos: cursor is null"
                )
                return listOf()
            }

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val bucketNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val buckedIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)

            val videos = arrayListOf<MediaItemModel>()
            while (cursor.moveToNext()) {
                val id = idColumn.let { cursor.getLong(it) }
                val path = dataColumn.let { cursor.getString(it) }
                val bucket = bucketNameColumn.let { cursor.getString(it) }
                val bucketId = buckedIdColumn.let { cursor.getLong(it) }
                val date = dateAddedColumn.let { cursor.getLong(it) }

                videos.add(
                    MediaItemModel(
                        id = id,
                        path = path,
                        type = MediaItemType.VIDEO,
                        bucketId = bucketId,
                        bucketName = bucket,
                        dateAdded = date,
                    )
                )
            }
            cursor.close()
            return videos
        } catch (e: Throwable) {
            Log.d(MediaRepositoryImpl::class.java.simpleName, "failed getVideos: ${e.message}")
            return listOf()
        }
    }

    override fun getVideos(context: Context, albumId: Long): List<MediaItemModel> {
        try {
            val cursor: Cursor? = getVideoCursor(
                context,
                selection = "${MediaStore.Video.Media.BUCKET_ID} = $albumId",
                selectionArgs = null
            )
            if (cursor == null) {
                Log.d(
                    MediaRepositoryImpl::class.java.simpleName,
                    "warn getPhotos: cursor is null"
                )
                return listOf()
            }

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val bucketNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val buckedIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)

            val videos = arrayListOf<MediaItemModel>()
            while (cursor.moveToNext()) {
                val id = idColumn.let { cursor.getLong(it) }
                val path = dataColumn.let { cursor.getString(it) }
                val bucket = bucketNameColumn.let { cursor.getString(it) }
                val bucketId = buckedIdColumn.let { cursor.getLong(it) }
                val date = dateAddedColumn.let { cursor.getLong(it) }

                videos.add(
                    MediaItemModel(
                        id = id,
                        path = path,
                        type = MediaItemType.VIDEO,
                        bucketId = bucketId,
                        bucketName = bucket,
                        dateAdded = date,
                    )
                )
            }
            cursor.close()
            return videos
        } catch (e: Throwable) {
            Log.d(
                MediaRepositoryImpl::class.java.simpleName,
                "failed getVideos, albumId: $albumId -> ${e.message}"
            )
            return listOf()
        }
    }

    // reference [https://stackoverflow.com/a/68952524/9399863]
    fun getPhotos(context: Context, offset: Int): List<MediaItemModel> {
        return listOf()
//        try {
//            val projections = arrayOf(
//                MediaStore.Images.Media._ID,
//                MediaStore.Images.Media.DATA,
//                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
//                MediaStore.Images.Media.BUCKET_ID,
//                MediaStore.Images.Media.DATE_ADDED,
//            )
//
//            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
//
//            val cursor: Cursor?
//            when {
//                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
//                    val bundle = Bundle().apply {
//                        putInt(ContentResolver.QUERY_ARG_LIMIT, 20)
//                        putInt(ContentResolver.QUERY_ARG_OFFSET, 0)
//                        putStringArray(
//                            ContentResolver.QUERY_ARG_SORT_COLUMNS,
//                            arrayOf(MediaStore.Images.Media.DATE_ADDED)
//                        )
//                        putInt(
//                            ContentResolver.QUERY_ARG_SORT_DIRECTION,
//                            ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
//                        )
//                    }
//                    cursor = context.contentResolver.query(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        projections,
//                        bundle,
//                        null,
//                    )
//                }
//
//                else -> {
//                    cursor = context.contentResolver.query(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        projections,
//                        null,
//                        null,
//                        sortOrder,
//                    )
//                }
//            }
//
//            val idColumn = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//            val dataColumn = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            val bucketNameColumn =
//                cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
//            val buckedIdColumn = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
//            val dateAddedColumn = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
//
//            val images = arrayListOf<MediaItemModel>()
//            while (cursor?.moveToNext() == true) {
//                val id = idColumn?.let { cursor.getLong(it) }
//                val path = dataColumn?.let { cursor.getString(it) }
//                val bucket = bucketNameColumn?.let { cursor.getString(it) }
//                val bucketId = buckedIdColumn?.let { cursor.getLong(it) }
//                val date = dateAddedColumn?.let { cursor.getLong(it) }
//
//                if (id != null && path != null) {
//                    images.add(
//                        MediaItemModel(
//                            id = id,
//                            path = path,
//                            type = MediaItemType.IMAGE,
//                            bucketId = bucketId,
//                            bucketName = bucket,
//                            dateAdded = date,
//                        )
//                    )
//                }
//            }
//            cursor?.close()
//            return images
//        } catch (e: Throwable) {
//            Log.d(MediaRepositoryImpl::class.java.simpleName, "failed getPhotos: ${e.message}")
//            return listOf()
//        }
    }
}