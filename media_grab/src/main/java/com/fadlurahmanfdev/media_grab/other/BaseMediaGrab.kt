package com.fadlurahmanfdev.media_grab.other

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.fadlurahmanfdev.media_grab.constant.MediaGrabExceptionConstant
import com.fadlurahmanfdev.media_grab.data.model.MediaItemModelV2

abstract class BaseMediaGrab {
    fun getPhotoCursor(context: Context): Cursor? {
        val projections = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATE_ADDED,
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        return context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projections,
            null,
            null,
            sortOrder,
        )
    }

    fun getPhotoCursor(
        context: Context,
        selection: String,
        selectionArgs: Array<String>?,
    ): Cursor? {
        val projections = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATE_ADDED,
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        return context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projections,
            selection,
            selectionArgs,
            sortOrder,
        )
    }

    fun getVideoCursor(context: Context): Cursor? {
        val projections = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.DATE_ADDED,
        )

        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        return context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projections,
            null,
            null,
            sortOrder,
        )
    }

    fun getVideoCursor(
        context: Context,
        selection: String,
        selectionArgs: Array<String>?,
    ): Cursor? {
        val projections = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.DATE_ADDED,
        )

        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        return context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projections,
            selection,
            selectionArgs,
            sortOrder,
        )
    }

    fun getIntentPickImage(allowMultiple: Boolean = false): Intent {
        return Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).apply {
            setType("image/*")
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
        }
    }

    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        when {
            DocumentsContract.isDocumentUri(context, uri) -> {
                when {
                    isExternalStorageDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        // This is for checking Main Memory
                        return if ("primary".equals(type, ignoreCase = true)) {
                            if (split.size > 1) {
                                Environment.getExternalStorageDirectory()
                                    .toString() + "/" + split[1]
                            } else {
                                Environment.getExternalStorageDirectory().toString() + "/"
                            }
                            // This is for checking SD Card
                        } else {
                            "storage" + "/" + docId.replace(":", "/")
                        }
                    }

                    isMediaDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null
                        when (type) {
                            "image" -> {
                                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }

                            "video" -> {
                                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }

                            "audio" -> {
                                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                        }
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])

                        if (contentUri == null) return null


                        return getDataColumn(context, contentUri, selection, selectionArgs)
                    }

                    else -> {
                        return null
                    }
                }
            }

            "content".equals(uri.scheme, ignoreCase = true) -> {
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context,
                    uri,
                    null,
                    null,
                )
            }

            else -> {
                throw Exception()
            }
        }
    }

    fun getMediaItemModelFromUri(context: Context, uri: Uri): MediaItemModelV2? {
        when {
            DocumentsContract.isDocumentUri(context, uri) -> {
                when {
                    isExternalStorageDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        // This is for checking Main Memory
                        return if ("primary".equals(type, ignoreCase = true)) {
                            if (split.size > 1) {
                                Environment.getExternalStorageDirectory()
                                    .toString() + "/" + split[1]
                                MediaItemModelV2()
                            } else {
                                Environment.getExternalStorageDirectory().toString() + "/"
                                MediaItemModelV2()
                            }
                            // This is for checking SD Card
                        } else {
                            "storage" + "/" + docId.replace(":", "/")
                            MediaItemModelV2()
                        }
                    }

                    isMediaDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null
                        when (type) {
                            "image" -> {
                                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }

                            "video" -> {
                                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }

                            "audio" -> {
                                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                        }
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])

                        if (contentUri == null) return null


                        return getMediaItemFromCursor(
                            context,
                            contentUri,
                            selection,
                            selectionArgs
                        )
                    }

                    else -> {
                        return null
                    }
                }
            }

            "content".equals(uri.scheme, ignoreCase = true) -> {
                return if (isGooglePhotosUri(uri)) MediaItemModelV2() else getMediaItemFromCursor(
                    context,
                    uri,
                    null,
                    null,
                )
            }

            else -> {
                throw Exception()
            }
        }
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?,
    ): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA
        )

        try {
            cursor = context.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                null
            )

            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                return cursor.getString(index)
            }
        } catch (e: Throwable) {
            Log.e(this::class.java.simpleName, "MediaGrab-LOG %%% failed: ${e.message}")
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun getMediaItemFromCursor(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?,
    ): MediaItemModelV2? {
        val cursor: Cursor?

        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.BUCKET_ID,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.DATE_MODIFIED,
            MediaStore.MediaColumns.DATE_TAKEN,
            MediaStore.MediaColumns.RESOLUTION,
        )

        try {
            cursor = context.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                null
            )

            if (cursor == null) throw MediaGrabExceptionConstant.CANNOT_INITIALIZED_CURSOR

        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "MediaGrab-LOG %%% failed get cursor", t)
            throw MediaGrabExceptionConstant.CANNOT_INITIALIZED_CURSOR
        }

        try {
            if (cursor.moveToFirst()) {
                return MediaItemModelV2(
                    id = getId(cursor),
                    path = getData(cursor),
                    bucketId = getBucketId(cursor),
                    bucketName = getBucketName(cursor),
                    dateAdded = getDateAdded(cursor),
                    dateTaken = getDateTaken(cursor),
                    dateModified = getDateModified(cursor),
                    resolution = getResolution(cursor),
                )
            }
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "MediaGrab-LOG %%% failed get data", t)
        } finally {
            cursor.close()
        }
        return null
    }

    private fun getId(cursor: Cursor): Long? =
        getLongColumn(cursor, MediaStore.MediaColumns._ID)

    private fun getBucketId(cursor: Cursor): Long? =
        getLongColumn(cursor, MediaStore.MediaColumns.BUCKET_ID)

    private fun getData(cursor: Cursor): String? =
        getStringColumn(cursor, MediaStore.MediaColumns.DATA)

    private fun getBucketName(cursor: Cursor): String? =
        getStringColumn(cursor, MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)

    private fun getDateAdded(cursor: Cursor): Long? =
        getLongColumn(cursor, MediaStore.MediaColumns.DATE_ADDED)

    private fun getDateTaken(cursor: Cursor): Long? =
        getLongColumn(cursor, MediaStore.MediaColumns.DATE_TAKEN)

    private fun getDateModified(cursor: Cursor): Long? =
        getLongColumn(cursor, MediaStore.MediaColumns.DATE_MODIFIED)

    private fun getResolution(cursor: Cursor): String? =
        getStringColumn(cursor, MediaStore.MediaColumns.RESOLUTION)

    private fun getLongColumn(cursor: Cursor, column: String): Long? {
        return try {
            val index = cursor.getColumnIndexOrThrow(column)
            cursor.getLongOrNull(index)
        } catch (e: Throwable) {
            null
        }
    }

    private fun getStringColumn(cursor: Cursor, column: String): String? {
        return try {
            val index = cursor.getColumnIndexOrThrow(column)
            cursor.getStringOrNull(index)
        } catch (e: Throwable) {
            null
        }
    }

    private fun validatePermission(context: Context, permission: String) {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw MediaGrabExceptionConstant.EXTERNAL_STORAGE_PERMISSION_NOT_GRANTED
        }
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}