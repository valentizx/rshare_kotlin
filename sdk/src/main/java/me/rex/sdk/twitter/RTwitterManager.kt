package me.rex.sdk.twitter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.tweetcomposer.ComposerActivity
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import me.rex.sdk.share.*
import me.rex.sdk.util.*
import java.net.MalformedURLException
import java.net.URL

private const val TAG = "RTwitterManager"

private fun getComposerBuilder(context: Context,
                               text: String?,
                               imageUri: Uri?,
                               url : URL?) : TweetComposer.Builder {
    if (text == null && (imageUri != null || url != null)) {
        if (imageUri == null) {
            return TweetComposer.Builder(context)
                    .url(url)
        } else if (url == null) {
            return TweetComposer.Builder(context)
                    .image(imageUri)
        } else {
            return TweetComposer.Builder(context)
                    .image(imageUri)
                    .url(url)
        }
    } else if (imageUri == null && (text != null || url != null)) {
        if (text == null) {
            return TweetComposer.Builder(context)
                    .url(url)
        } else if (url == null) {
            return TweetComposer.Builder(context)
                    .text(text)
        } else {
            return TweetComposer.Builder(context)
                    .url(url)
                    .text(text)
        }
    } else if (url == null && (imageUri != null || text != null)) {
        if (imageUri == null) {
            return TweetComposer.Builder(context)
                    .text(text)
        } else if (text == null) {
            return TweetComposer.Builder(context)
                    .image(imageUri)
        } else {
            return TweetComposer.Builder(context)
                    .text(text)
                    .image(imageUri)
        }

    } else {
        return TweetComposer.Builder(context)
                .text(text)
                .image(imageUri)
                .url(url)
    }

}

fun getComposerIntent(session: TwitterSession,
                      context: Context,
                      text: String?,
                      imageUri: Uri?,
                      hashTag: String?) : Intent {
    hashTag?.let {
        val intent = ComposerActivity.Builder(context)
                .session(session)
                .text(text)
                .image(imageUri)
                .hashtags(hashTag)
                .createIntent()
        return intent
    }
    val intent = ComposerActivity.Builder(context)
            .session(session)
            .text(text)
            .image(imageUri)
            .createIntent()
    return intent

}

class RTwitterManager private constructor() : RShare() {

    companion object {
        val instance : RTwitterManager get() = Inner.instance
    }
    private object Inner {
        val instance = RTwitterManager()
    }

    var context : Context? = null
        private set

    var shareCallback : RShareCallback? = null
        private set


    internal fun sdkInitialize(context: Context) {

        val key = getTwitterConsumerKey(context)
        val secret = getTwitterConsumerSecret(context)

        val config = TwitterConfig.Builder(context)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig(key, secret))
                .debug(true)
                .build()
        Twitter.initialize(config)
    }

    fun share(context: Context,
              webpageUrl : String?,
              text : String?,
              image : Bitmap?,
              hashTag : String?,
              mode: Mode,
              callback: RShareCallback?) {

        if (!isInstalled(context, RSharePlatform.Twitter)) {
            Log.e(TAG, "Twitter 未安装")
            return
        }

        if (webpageUrl.isNullOrEmpty() && text.isNullOrEmpty() && image == null) {
            Log.e(TAG, "分享参数中: 网页链接、文字描述以及图片不能同时为空")
            return
        }


        image?.let {
            deleteExternalShareDir(context)
            saveBitmapToExternalSharePath(context, image)
        }

        if (mode == Mode.Feed || mode == Mode.Web || mode == Mode.Automatic) {
            sdkInitialize(context)
            this.context = context
            if (RTwitterAuthHelper.instance.hasLogged) {
                this.shareCallback = callback
                val session = TwitterCore.getInstance().sessionManager.activeSession
                val intent = getComposerIntent(session, context, text,getExternalSharePathFileUris
                (context)?.get(0), hashTag)
                context.startActivity(intent)
            } else {
                RTwitterAuthHelper.instance.authorizeTwitter(context) { state ->
                    when (state) {
                        1 -> {
                            val session = TwitterCore.getInstance().sessionManager.activeSession
                            val intent = getComposerIntent(session, context, text,getExternalSharePathFileUris
                            (context)?.get(0), hashTag)
                            context.startActivity(intent)
                        }
                        0 -> {
                            shareCallback?.invoke(RSharePlatform.Twitter, ShareState.Failure, "Twitter 授权失败")
                        }
                    }

                }
            }
        } else {

            detectFileUriExposure()

            var url : URL
            var imageUri : Uri?
            try {
                url = URL(webpageUrl)
                imageUri = getExternalSharePathFileUris(context)?.get(0)

                getComposerBuilder(context, text, imageUri, url).show()

            } catch (e : MalformedURLException) {
                e.printStackTrace()

            }

        }
    }


}