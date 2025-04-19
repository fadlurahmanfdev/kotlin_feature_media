package com.fadlurahmanfdev.media_grab.exception

data class MediaGrabException(
    val code:String,
    override val message: String?
):Throwable(message = message)
