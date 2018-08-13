package me.rex.sdk.whatsapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import me.rex.sdk.share.RShare
import me.rex.sdk.share.RSharePlatform
import me.rex.sdk.util.detectFileUriExposure
import me.rex.sdk.util.getExternalSharePathFileUris
import me.rex.sdk.util.isInstalled
import me.rex.sdk.util.saveBitmapToExternalSharePath

private const val TAG = "RWhatsAppManager==>"

class RWhatsAppManager private constructor() : RShare() {
    companion object {
        val instance : RWhatsAppManager get() = Inner.instance
    }
    private object Inner {
        val instance = RWhatsAppManager()
    }

    fun share(context: Context,
              image : Bitmap?,
              description : String?) {
        if (!isInstalled(context, RSharePlatform.WhatsApp)) {
            Log.e(TAG, "WhatsApp 客户端未安装.")
        }
        if (image == null && description.isNullOrEmpty()) {
            Log.e(TAG, "分享参数无效.")
        }

        detectFileUriExposure()

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        if (description != null) {
            intent.putExtra(Intent.EXTRA_TEXT, description)
            intent.type = "text/plain"
        }

        if (image != null) {
            saveBitmapToExternalSharePath(context, image)
            intent.putExtra(Intent.EXTRA_STREAM, getExternalSharePathFileUris(context)?.get(0))
            intent.type = "image/jpeg"
        }
        intent.setPackage("com.whatsapp")
        context.startActivity(intent)
    }
}