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
}