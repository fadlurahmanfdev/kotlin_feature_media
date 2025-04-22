package com.fadlurahmanfdev.media_grab.constant

import com.fadlurahmanfdev.media_grab.exception.MediaGrabException

object MediaGrabExceptionConstant {
    val EXTERNAL_STORAGE_PERMISSION_NOT_GRANTED = MediaGrabException(
        code = "EXTERNAL_STORAGE_PERMISSION_NOT_GRANTED",
        message = "External storage permission not granted"
    )

    val CANNOT_INITIALIZED_CURSOR = MediaGrabException(
        code = "CANNOT_INITIALIZED_CURSOR",
        message = "Unable to initialized cursor, probably caused by nullable cursor"
    )

    val UNABLE_GET_ID = MediaGrabException(
        code = "UNABLE_GET_ID",
        message = "Unable to get id"
    )
    val UNABLE_GET_REAL_PATH = MediaGrabException(
        code = "UNABLE_GET_REAL_PATH",
        message = "Unable to get real path of an item"
    )
    val UNABLE_GET_DISPLAY_NAME = MediaGrabException(
        code = "UNABLE_GET_DISPLAY_NAME",
        message = "Unable to get display name of an item"
    )
}