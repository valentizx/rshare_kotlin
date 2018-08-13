package me.rex.sdk.instagram

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.util.Log
import me.rex.sdk.share.Mode
import me.rex.sdk.share.RShare
import me.rex.sdk.share.RSharePlatform
import me.rex.sdk.util.*

private const val TAG = "RInstagramManager==>"

class RInstagramManager private constructor() : RShare() {
    companion object {
        val instance : RInstagramManager get() = Inner.instance
    }
    private object Inner {
        val instance = RInstagramManager()
    }

    fun shareVideo(context: Context, localVideoUrl : Uri) {

        if (!isInstalled(context, RSharePlatform.Instagram)) {
            Log.e(TAG, "Instagram 未安装")
            return
        }
        detectFileUriExposure()

        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("video/*")
        intent.putExtra(Intent.EXTRA_STREAM, localVideoUrl)
        intent.setPackage("com.instagram.android")
        context.startActivity(intent)

    }

    fun shareImage(context: Context, image : Bitmap, mode : Mode) {
        if (!isInstalled(context, RSharePlatform.Instagram)) {
            Log.e(TAG, "Instagram 未安装")
            return
        }

        deleteExternalShareDir(context)
        saveBitmapToExternalSharePath(context, image)

        if (mode == Mode.System) {
            var imageUri : Uri = Uri.parse("")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val authority = context.packageName + ".fileprovider"
                getExternalSharePathFiles(context)?.let {

                    imageUri = FileProvider.getUriForFile(context, authority,
                            getExternalSharePathFiles(context)?.get(0)!!)
                }

            } else {
                imageUri = getExternalSharePathFileUris(context)?.get(0)!!
            }

            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("image/jpeg")
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_STREAM, imageUri)
            context.startActivity(Intent.createChooser(intent, "分享"))
        } else {

            detectFileUriExposure()
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("image/*")
            intent.putExtra(Intent.EXTRA_STREAM, getExternalSharePathFileUris(context)?.get(0)!!)
            intent.setPackage("com.instagram.android")
            context.startActivity(intent)


        }
    }
}