package me.rex.sdk.qq

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import me.rex.sdk.RShare
import me.rex.sdk.RShareCallback
import me.rex.sdk.RSharePlatform
import me.rex.sdk.ShareContentType
import me.rex.sdk.util.*


private const val TAG = "RQqManager ==>"

class RQqManager private constructor() : RShare()  {


    companion object {
        val instance : RQqManager get() = Inner.instance
    }
    private object Inner {
        val instance = RQqManager()
    }


    internal lateinit var context : Context
        private set

    internal var shareCallback : RShareCallback? = null
        private set



    fun shareWebpage(context: Context,
                     webpageUrl : String,
                     title : String,
                     description: String?,
                     thumbImageUrl : String?,
                     callback: RShareCallback?) {

        if (isInstalled(context, RSharePlatform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.")

            return
        }
        this.context = context
        shareCallback = callback

        val intent = Intent(context, RQqActivity::class.java)

        intent.putExtra("webpageUrl", webpageUrl)
        intent.putExtra("title", title)
        intent.putExtra("description", description)
        intent.putExtra("thumbImage", thumbImageUrl)
        intent.putExtra("type", ShareContentType.Webpage)
        context.startActivity(intent)
    }

    fun shareImage(context: Context,
                   image : Bitmap,
                   callback: RShareCallback?) {

        if (isInstalled(context, RSharePlatform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.")

            return
        }
        this.context = context
        shareCallback = callback

        deleteExternalShareDir(context)
        saveBitmapToExternalSharePath(context, image)

        val intent = Intent(context, RQqActivity::class.java)
        intent.putExtra("type", ShareContentType.Photo)
        context.startActivity(intent)

    }

    fun shareMusic(context: Context,
                   audioStreamUrl : String,
                   targetUrl: String,
                   title : String,
                   description: String?,
                   thumbImageUrl : String?,
                   callback: RShareCallback?) {

        if (isInstalled(context, RSharePlatform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.")

            return
        }

        this.context = context
        shareCallback = callback
        val intent = Intent(context, RQqActivity::class.java)
        intent.putExtra("musicUrl", audioStreamUrl)
        intent.putExtra("targetUrl", targetUrl)
        intent.putExtra("title", title)
        intent.putExtra("description", description)
        intent.putExtra("thumbImage", thumbImageUrl)
        intent.putExtra("type", ShareContentType.Music)
        context.startActivity(intent)
    }


    fun shareApp(context: Context,
                 appUrl : String,
                 title : String,
                 description: String?,
                 thumbImageUrl: String?,
                 callback: RShareCallback?) {
        if (isInstalled(context, RSharePlatform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.")

            return
        }

        this.context = context
        shareCallback = callback

        val intent = Intent(context, RQqActivity::class.java)
        intent.putExtra("targetUrl", appUrl)
        intent.putExtra("title", title)
        intent.putExtra("description", description)
        intent.putExtra("thumbImage", thumbImageUrl)
        intent.putExtra("type", ShareContentType.App)
        context.startActivity(intent)

    }

    fun shareWebpageToZone(context: Context,
                           webpageUrl: String,
                           title: String,
                           description: String?,
                           images : ArrayList<String>,
                           callback: RShareCallback?) {

        if (isInstalled(context, RSharePlatform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.")

            return
        }

        this.context = context
        shareCallback = callback

        val intent = Intent(context, RQZoneActivity::class.java)
        intent.putExtra("webpageUrl", webpageUrl)
        intent.putExtra("title", title)
        intent.putExtra("description", description)

        intent.putStringArrayListExtra("images", images)
        intent.putExtra("type", ShareContentType.Webpage)
        context.startActivity(intent)

    }

    fun publishImagesToZone(context: Context,
                            localPhotos : ArrayList<Bitmap>,
                            description: String?,
                            callback: RShareCallback?) {
        if (isInstalled(context, RSharePlatform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.")

            return
        }
        if (localPhotos.size > 9){
            Log.e(TAG, "至多可上传 9 张图片!")

            return
        }
        this.context = context
        shareCallback = callback

        val intent = Intent(context, RQZoneActivity::class.java)
        intent.putExtra("description", description)

        deleteExternalShareDir(context)
        for (i in 0 until localPhotos.size) {
            saveBitmapToExternalSharePath(context, localPhotos.get(i))
        }

        val paths = getExternalSharePathFilePaths(context)
        intent.putStringArrayListExtra("images", paths)
        intent.putExtra("type", ShareContentType.Photo)
        context.startActivity(intent)


    }

    fun publishVideoToZone(context: Context,
                           localVideoUrl : Uri,
                           description: String?,
                           callback: RShareCallback?) {

        if (isInstalled(context, RSharePlatform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.")

            return
        }
        this.context = context
        shareCallback = callback

        val path = getPath(context, localVideoUrl)

        val intent = Intent(context, RQZoneActivity::class.java)
        intent.putExtra("description", description)
        intent.putExtra("type", ShareContentType.Video)
        intent.putExtra("local_video_path", path)
        context.startActivity(intent)


    }





}