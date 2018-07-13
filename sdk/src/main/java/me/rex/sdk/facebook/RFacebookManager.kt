package me.rex.sdk.facebook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import android.util.Log
import me.rex.sdk.*
import me.rex.sdk.util.deleteExternalShareDir
import me.rex.sdk.util.isInstalled
import me.rex.sdk.util.saveBitmapToExternalSharePath
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.collections.ArrayList

private const val TAG = "RFacebookManager===>"



class RFacebookManager private constructor(): RShare() {
    companion object {
        val instance : RFacebookManager get() = Inner.instance
    }
    private object Inner {
        val instance = RFacebookManager()
    }

    internal lateinit var context : Context
        private set

    internal var shareCallback: RShareCallback? = null



    fun printKeyHash (context : Activity) : String {

        var packageInfo : PackageInfo?
        var key : String? = ""
        try {

            var packageName = context.applicationContext.packageName
            packageInfo = context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)

            packageInfo?.let {
                for (s in packageInfo.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(s.toByteArray())
                    key = String(Base64.encode(md.digest(), 0))
                    Log.e("Key Hash:", key)

                }
            }

        } catch (e : PackageManager.NameNotFoundException) {
            Log.e("Name not found", e.toString())
        } catch (e : NoSuchAlgorithmException) {
            Log.e("No such an algorithm", e.toString())
        } catch (e : Exception) {
            Log.e("Exception", e.toString())
        }
        return key ?: ""
    }


    fun shareWebpage(context: Context,
                     webpageUrl : String,
                     quote : String?,
                     hashTag : String?,
                     mode : Mode,
                     callback: RShareCallback?) {

        if (mode == Mode.Native && !isInstalled(context, RSharePlatform.Facebook)) {
            Log.e(TAG, "Facebook 客户端未安装, 更换其他 Mode 分享.")
            return
        }
        this.context = context
        callback?.let {
            shareCallback = callback
        }

        val intent = Intent(context, RFacebookActivity::class.java)

        intent.putExtra("webpageUrl" , webpageUrl)
        quote?.let {
            intent.putExtra("quote", quote)
        }
        hashTag?.let {
            intent.putExtra("hashTag", hashTag)
        }
        intent.putExtra("mode", mode)
        intent.putExtra("type", ShareContentType.Webpage)
        context.startActivity(intent)

    }

    fun sharePhoto(context: Context, photos : ArrayList<Bitmap>){

        if (!isInstalled(context, RSharePlatform.Facebook)) {
            Log.e(TAG, "Facebook 客户端未安装.")
            return
        }
        if (photos.size > 6) {
            Log.e(TAG, "图片不能超过6张!")
            return
        }
        this.context = context

        val intent = Intent(context, RFacebookActivity::class.java)
        deleteExternalShareDir(context)
        for (i in 0 until photos.size) {
            saveBitmapToExternalSharePath(context, photos.get(i))
        }
        intent.putExtra("type", ShareContentType.Photo)
        context.startActivity(intent)

    }

    fun shareLocalVideo(context: Context, localVideoUrl : Uri) {
        if (!isInstalled(context, RSharePlatform.Facebook)) {
            Log.e(TAG, "Facebook 客户端未安装.")
            return
        }
        this.context = context
        val intent = Intent(context, RFacebookActivity::class.java)
        intent.putExtra("type", ShareContentType.Video)
        intent.putExtra("localVideoPath", localVideoUrl)
        context.startActivity(intent)

    }
}