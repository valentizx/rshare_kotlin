package me.rex.sdk.sina

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.share.WbShareHandler
import me.rex.sdk.share.RShare
import me.rex.sdk.share.RShareCallback
import me.rex.sdk.share.RSharePlatform
import me.rex.sdk.share.ShareContentType
import me.rex.sdk.util.*


private const val TAG = "RSinaWeiboManager==>"

class RSinaWeiboManager private constructor() : RShare() {

    companion object {
        val instance : RSinaWeiboManager get() = Inner.instance
    }
    private object Inner {
        val instance = RSinaWeiboManager()
    }

    internal var shareCallback : RShareCallback? = null
        private set

    internal lateinit var context : Context
        private set


    internal fun sdkIntiialize(context : Context) {

        val key = getSinaAppKey(context)
        val redirectUrl = getSinaAppRedirectUrl(context)
        val scope = getSinaScope(context)

        WbSdk.install(context, AuthInfo(context, key, redirectUrl, scope))

        val handler = WbShareHandler(context as Activity)
        handler.registerApp()
    }


    fun shareText(context: Context,
                  text : String,
                  callback: RShareCallback?) {

        if (!isInstalled(context, RSharePlatform.Sina)) {
            Log.e(TAG, "新浪微博未安装")
            return
        }
        sdkIntiialize(context)
        shareCallback = callback
        this.context = context

        val intent = Intent(context, RSinaWeiboActivity::class.java)
        intent.putExtra("text", text)
        intent.putExtra("type", ShareContentType.Text)
        context.startActivity(intent)

    }

    fun sharePhoto(context: Context,
                   photos : ArrayList<Bitmap>,
                   text : String?,
                   isToStory : Boolean,
                   callback: RShareCallback?) {

        if (!isInstalled(context, RSharePlatform.Sina)) {
            Log.e(TAG, "新浪微博未安装")
            return
        }
        if (isToStory && photos.size > 1) {
            Log.e(TAG, "「分享到微博故事」功能开启的情况下, 图片只能传一张!!!")
            return
        }
        if (!isToStory && photos.size == 0 || !isToStory && photos.size > 9) {
            Log.e(TAG, "分享到微博的图片数目不可以为0或者大于9!!!")
            return
        }

        sdkIntiialize(context)

        shareCallback = callback
        this.context = context

        val cls = if (isToStory) RSinaWeiboStoryActivity::class.java else RSinaWeiboActivity::class.java

        val intent = Intent(context, cls)
        intent.putExtra("text", text)
        deleteExternalShareDir(context)
        for (i in 0 until photos.size) {
            saveBitmapToExternalSharePath(context,photos.get(i))
        }
        intent.putExtra("type", ShareContentType.Photo)
        context.startActivity(intent)


    }

    fun shareWebpage(context: Context,
                     webpageUrl : String,
                     title : String?,
                     description: String?,
                     thumbImage : Bitmap?,
                     callback: RShareCallback?) {

        if (!isInstalled(context, RSharePlatform.Sina)) {
            Log.e(TAG, "新浪微博未安装")
            return
        }

        sdkIntiialize(context)
        shareCallback = callback
        this.context = context

        val intent = Intent(context, RSinaWeiboActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("description", description)
        intent.putExtra("webpage", webpageUrl)
        thumbImage?.let {
            deleteExternalShareDir(context)
            saveBitmapToExternalSharePath(context, thumbImage)
        }
        intent.putExtra("type", ShareContentType.Webpage)
        context.startActivity(intent)

    }

    fun shareLocalVideo(context: Context,
                        localVideoUrl: Uri,
                        description: String?,
                        isToStory: Boolean,
                        callback: RShareCallback?) {
        if (!isInstalled(context, RSharePlatform.Sina)) {
            Log.e(TAG, "新浪微博未安装")
            return
        }

        sdkIntiialize(context)
        shareCallback = callback
        this.context = context

        val cls = if (isToStory) RSinaWeiboStoryActivity::class.java else RSinaWeiboActivity::class.java

        val intent = Intent(context, cls)
        intent.putExtra("text", description)
        intent.putExtra("local_video_uri", localVideoUrl)
        intent.putExtra("type", ShareContentType.Video)
        context.startActivity(intent)
    }



}