package me.rex.sdk.tumblr

import android.content.Context
import android.content.Intent
import me.rex.sdk.share.RShare
import me.rex.sdk.share.RShareCallback
import me.rex.sdk.share.ShareContentType

class RTumblrManager private constructor() : RShare() {
    companion object {
        val instance : RTumblrManager get() = Inner.instance
    }
    private object Inner {
        val instance = RTumblrManager()
    }


    internal var shareCallback : RShareCallback? = null
    internal lateinit var context: Context



    fun shareImage(context: Context,
                   imageUrl : String,
                   description: String?,
                   webpageUrl : String?,
                   callback: RShareCallback?) {

        shareCallback = callback
        this.context = context

        val intent = Intent(context, RTumblrActivity::class.java)
        intent.putExtra("imageUrl", imageUrl)
        intent.putExtra("description", description)
        intent.putExtra("webpageUrl", webpageUrl)
        intent.putExtra("type", ShareContentType.Photo)
        context.startActivity(intent)
    }

    fun shareText(context: Context,
                  body : String,
                  title : String?,
                  webpageUrl : String?,
                  callback: RShareCallback?) {

        shareCallback = callback
        this.context = context

        val intent = Intent(context, RTumblrActivity::class.java)
        intent.putExtra("body", body)
        intent.putExtra("title", title)
        intent.putExtra("webpageUrl", webpageUrl)
        intent.putExtra("type", ShareContentType.Text)
        context.startActivity(intent)

    }

}