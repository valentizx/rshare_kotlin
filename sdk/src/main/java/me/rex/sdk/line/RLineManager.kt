package me.rex.sdk.line

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import me.rex.sdk.RShare
import me.rex.sdk.RSharePlatform
import me.rex.sdk.util.detectFileUriExposure
import me.rex.sdk.util.getExternalSharePathFileUris
import me.rex.sdk.util.isInstalled
import me.rex.sdk.util.saveBitmapToExternalSharePath

private const val TAG : String = "RLineManager"

class RLineManager private constructor() : RShare() {
    //

    companion object {
        val instance : RLineManager get() = Inner.instance
    }
    private object Inner {
        val instance = RLineManager()
    }

    fun share(context: Context, text : String) {

        if (!isInstalled(context, RSharePlatform.Line)) {
            Log.e(TAG, "Line 未安装")
            return
        }


        val intent = Intent()
        intent.setAction(Intent.ACTION_SEND)

        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.setType("text/*")

        intent.setPackage("jp.naver.line.android")
        context.startActivity(intent)

    }
    fun share(context: Context, image : Bitmap) {
        if (!isInstalled(context, RSharePlatform.Line)) {
            Log.e(TAG, "Line 未安装")
            return
        }

        detectFileUriExposure()
        saveBitmapToExternalSharePath(context, image)

        val intent = Intent()
        intent.setAction(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, getExternalSharePathFileUris(context)?.get(0))
        intent.setType("image/*")
        intent.setPackage("jp.naver.line.android")
        context.startActivity(intent)
    }

}