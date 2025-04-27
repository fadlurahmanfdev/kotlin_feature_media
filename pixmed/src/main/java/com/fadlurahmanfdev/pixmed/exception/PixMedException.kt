package com.fadlurahmanfdev.pixmed.exception

data class PixMedException(
    val code:String,
    override val message: String?
):Throwable(message = message)
