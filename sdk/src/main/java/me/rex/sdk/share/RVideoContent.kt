package me.rex.sdk.share

import android.graphics.Bitmap
import android.net.Uri

class RVideoContent {
    lateinit var localVideoUrl : Uri
    var videoWebapgeUrl : String? = null
    var title : String? = null
    var quote : String? = null
    var thumbImage : Bitmap? = null
}