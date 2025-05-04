package com.fadlurahmanfdev.pixmed.constant

import com.fadlurahmanfdev.pixmed.exception.PixMedException

object PixMedExceptionConstant {
    val READ_MEDIA_IMAGES_PERMISSION_NOT_GRANTED = PixMedException(
        code = "READ_MEDIA_PERMISSION_NOT_GRANTED",
        message = "Read media images permission not granted"
    )
    val READ_EXTERNAL_STORAGE_PERMISSION_NOT_GRANTED = PixMedException(
        code = "READ_EXTERNAL_STORAGE_PERMISSION_NOT_GRANTED",
        message = "Read external storage permission not granted"
    )

    val CANNOT_INITIALIZED_CURSOR = PixMedException(
        code = "CANNOT_INITIALIZED_CURSOR",
        message = "Unable to initialized cursor, probably caused by nullable cursor"
    )

    val UNABLE_GET_ID = PixMedException(
        code = "UNABLE_GET_ID",
        message = "Unable to get id"
    )
    val UNABLE_GET_REAL_PATH = PixMedException(
        code = "UNABLE_GET_REAL_PATH",
        message = "Unable to get real path of an item"
    )
    val UNABLE_GET_DISPLAY_NAME = PixMedException(
        code = "UNABLE_GET_DISPLAY_NAME",
        message = "Unable to get display name of an item"
    )

    val INVALID_OFFSET = PixMedException(
        code = "INVALID_OFFSET",
        message = "invalid offset to fetch list of items"
    )

    val UNEXPECTED = PixMedException(
        code = "UNEXPECTED",
        message = "unexpected message"
    )
}