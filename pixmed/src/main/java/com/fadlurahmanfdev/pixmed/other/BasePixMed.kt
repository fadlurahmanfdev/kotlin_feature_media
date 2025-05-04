package com.fadlurahmanfdev.pixmed.other

import android.content.Context
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
import com.fadlurahmanfdev.pixmed.constant.PixMedExceptionConstant
import com.fadlurahmanfdev.pixmed.data.model.PixMedItem

abstract class BasePixMed {
    fun getPhotoCursor(context: Context): Cursor? = getPhotoCursor(context, null, null)

    fun getPhotoCursor(
        context: Context,
        selection: String?,
        selectionArgs: Array<String>?,
    ): Cursor? {
        val projections = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.RESOLUTION,
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

    fun getVideoCursor(context: Context): Cursor? = getVideoCursor(context, null, null)

    fun getVideoCursor(
        context: Context,
        selection: String?,
        selectionArgs: Array<String>?,
    ): Cursor? {
        val projections = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.RESOLUTION,
            MediaStore.Video.Media.DURATION,
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

    private fun getRealPathFromUri(context: Context, uri: Uri): String? {
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

    fun getMediaItemModelFromUri(context: Context, uri: Uri): PixMedItem? {
        when {
            DocumentsContract.isDocumentUri(context, uri) -> {
                Log.d(this::class.java.simpleName, "PixMedia-LOG %%% item uri is a document URI")
                when {
                    isExternalStorageDocument(uri) -> {
                        throw PixMedExceptionConstant.UNEXPECTED.copy(code = "UNIMPLEMENTED")
//                        Log.d(
//                            this::class.java.simpleName,
//                            "PixMedia-LOG %%% item is an external storage document"
//                        )
//                        val docId = DocumentsContract.getDocumentId(uri)
//                        val split = docId.split(":").toTypedArray()
//                        val type = split[0]
//                        // This is for checking Main Memory
//                        return if ("primary".equals(type, ignoreCase = true)) {
//                            if (split.size > 1) {
//                                Environment.getExternalStorageDirectory()
//                                    .toString() + "/" + split[1]
//                                MediaGrabItemModel(path = "fake", displayName = "fake", id = 11)
//                            } else {
//                                Environment.getExternalStorageDirectory().toString() + "/"
//                                MediaGrabItemModel(path = "fake", displayName = "fake", id = 2011)
//                            }
//                            // This is for checking SD Card
//                        } else {
//                            "storage" + "/" + docId.replace(":", "/")
//                            MediaGrabItemModel(path = "fake", displayName = "fake", id = 1021)
//                        }
                    }

                    isMediaDocument(uri) -> {
                        Log.d(
                            this::class.java.simpleName,
                            "PixMedia-LOG %%% item is a media document"
                        )
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

                    isGoogleDrivesUri(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        Log.d(this::class.java.simpleName, "PixMedia-LOG %%% doc id: $docId")
                        throw PixMedExceptionConstant.UNEXPECTED.copy(code = "UNIMPLEMENTED")
                    }

                    else -> {
                        throw PixMedExceptionConstant.UNEXPECTED.copy(code = "UNIMPLEMENTED")
                    }
                }
            }

            "content".equals(uri.scheme, ignoreCase = true) -> {
                Log.d(this::class.java.simpleName, "PixMedia-LOG %%% item uri scheme contain equal")
                return when {
                    isGooglePhotosUri(uri) -> {
                        PixMedItem(
                            id = 0L,
                            name = uri.path ?: "-",
                            path = uri.path ?: "-",
                        )
                    }

                    else -> {
                        getMediaItemFromCursor(
                            context,
                            uri,
                            null,
                            null,
                        )
                    }
                }
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
            Log.e(this::class.java.simpleName, "PixMedia-LOG %%% failed: ${e.message}")
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
    ): PixMedItem {
        val cursor: Cursor?

        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
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

            if (cursor == null) throw PixMedExceptionConstant.CANNOT_INITIALIZED_CURSOR

        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "PixMedia-LOG %%% failed get cursor", t)
            throw PixMedExceptionConstant.CANNOT_INITIALIZED_CURSOR
        }

        try {
            if (cursor.moveToFirst()) {
                val bucketId = getBucketId(cursor)
                val bucketName = getBucketName(cursor)
                val bucket: PixMedItem.Bucket? =
                    if (bucketId != null || bucketName != null) PixMedItem.Bucket(
                        id = bucketId,
                        name = bucketName
                    ) else null
                return PixMedItem(
                    id = getId(cursor),
                    path = getPath(cursor)
                        ?: throw PixMedExceptionConstant.UNABLE_GET_REAL_PATH,
                    name = getDisplayName(cursor)
                        ?: throw PixMedExceptionConstant.UNABLE_GET_DISPLAY_NAME,
                    dateAdded = getDateAdded(cursor),
                    type = getMimeType(cursor),
                    dateModified = getDateModified(cursor),
                    resolution = getResolution(cursor),
                    bucket = bucket,
                )
            } else {
                throw Exception()
            }
        } catch (t: Throwable) {
            Log.e(this::class.java.simpleName, "PixMedia-LOG %%% failed get data", t)
            throw t
        } finally {
            cursor.close()
        }
    }

    fun getId(cursor: Cursor): Long? =
        getLongColumn(cursor, MediaStore.MediaColumns._ID)

    fun getDisplayName(cursor: Cursor): String? =
        getStringColumn(cursor, MediaStore.MediaColumns.DISPLAY_NAME)

    fun getBucketId(cursor: Cursor): Long? =
        getLongColumn(cursor, MediaStore.MediaColumns.BUCKET_ID)

    fun getPath(cursor: Cursor): String? =
        getStringColumn(cursor, MediaStore.MediaColumns.DATA)

    fun getMimeType(cursor: Cursor): String? =
        getStringColumn(cursor, MediaStore.MediaColumns.MIME_TYPE)

    fun getDuration(cursor: Cursor): Long? =
        getLongColumn(cursor, MediaStore.MediaColumns.DURATION)

    fun getBucketName(cursor: Cursor): String? =
        getStringColumn(cursor, MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)

    fun getDateAdded(cursor: Cursor): Long? =
        getLongColumn(cursor, MediaStore.MediaColumns.DATE_ADDED)

    fun getDateTaken(cursor: Cursor): Long? =
        getLongColumn(cursor, MediaStore.MediaColumns.DATE_TAKEN)

    fun getDateModified(cursor: Cursor): Long? =
        getLongColumn(cursor, MediaStore.MediaColumns.DATE_MODIFIED)

    fun getResolution(cursor: Cursor): String? =
        getStringColumn(cursor, MediaStore.MediaColumns.RESOLUTION)

    fun getLongColumn(cursor: Cursor, column: String): Long? {
        return try {
            val index = cursor.getColumnIndexOrThrow(column)
            cursor.getLongOrNull(index)
        } catch (e: Throwable) {
            null
        }
    }

    fun getStringColumn(cursor: Cursor, column: String): String? {
        return try {
            val index = cursor.getColumnIndexOrThrow(column)
            cursor.getStringOrNull(index)
        } catch (e: Throwable) {
            Log.e(
                this::class.java.simpleName,
                "PixMedia-LOG %%% failed to get string column: $column",
                e
            )
            null
        }
    }

    fun validatePermission(context: Context, permission: String) {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw PixMedExceptionConstant.READ_MEDIA_IMAGES_PERMISSION_NOT_GRANTED
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

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Drive.
     */
    fun isGoogleDrivesUri(uri: Uri): Boolean {
        return "com.google.android.apps.docs.storage" == uri.authority
    }
}