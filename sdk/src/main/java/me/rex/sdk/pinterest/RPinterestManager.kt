package me.rex.sdk.pinterest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import me.rex.sdk.RShare
import me.rex.sdk.RSharePlatform
import me.rex.sdk.util.isInstalled

private const val TAG = "RPinterestManager==>"

class RPinterestManager private constructor(): RShare() {
    companion object {
        val instance : RPinterestManager get() = Inner.instance
    }
    private object Inner {
        val instance = RPinterestManager()
    }

    fun shareImage(context: Context,
                   imageUrl : String) {
        if (!isInstalled(context, RSharePlatform.Pinterest)) {
            Log.e(TAG, "Pinterest 未安装")
            return
        }

        val intent = Intent(Intent.ACTION_SEND)

        intent.setPackage("com.pinterest")
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUrl))

        intent.type = "*/*"
        context.startActivity(intent)


    }



}