package me.rex.sdk.sina

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.facebook.internal.Utility
import com.sina.weibo.sdk.api.*
import me.rex.sdk.util.getExternalSharePathBitmaps
import me.rex.sdk.util.getExternalSharePathFileUris

class RSinaWeiboHelper(val intent : Intent, val context: Context) {

    private val mIntent = intent
    private val mContext = context

    internal val textMessage : WeiboMultiMessage
        get() {
            val obj = TextObject()
            obj.text = if (mIntent.getStringExtra("text").isNullOrEmpty()) "" else mIntent.getStringExtra("text")
            obj.title = "share.rex.me"
            obj.actionUrl = "http://share.rex.me"

            val message = WeiboMultiMessage()
            message.textObject = obj
            return message
        }


    internal val imageMessage : WeiboMultiMessage
        get() {

            val textObj = TextObject()
            textObj.text = if (mIntent.getStringExtra("text").isNullOrEmpty()) "" else mIntent.getStringExtra("text")
            textObj.title = "share.rex.me"
            textObj.actionUrl = "http://share.rex.me"

            val multiImageObj = MultiImageObject()


            getExternalSharePathFileUris(mContext)?.let {
                multiImageObj.setImageList(getExternalSharePathFileUris(mContext))
            }

            val message = WeiboMultiMessage()
            message.textObject = textObj
            message.multiImageObject = multiImageObj
            return message


        }


    internal val webMessage : WeiboMultiMessage
        get() {

            val obj = WebpageObject()
            obj.identify = com.sina.weibo.sdk.utils.Utility.generateGUID()
            obj.title = mIntent.getStringExtra("title")
            obj.description = mIntent.getStringExtra("description")

            val bitmaps = getExternalSharePathBitmaps(mContext)
            bitmaps?.let {
                for (i in 0 until bitmaps.size) {
                    obj.setThumbImage(bitmaps.get(0))
                }
            }
            obj.actionUrl = mIntent.getStringExtra("webpage")
            obj.defaultText = "在未设置网页描述下默认提供的文字, 请设置 'description' 字段!"

            val message = WeiboMultiMessage()
            message.mediaObject = obj
            return message

        }


    internal val videoMessage : WeiboMultiMessage
        get() {

            val textObj = TextObject()
            textObj.text = if (mIntent.getStringExtra("text").isNullOrEmpty()) "" else mIntent.getStringExtra("text")
            textObj.title = "share.rex.me"
            textObj.actionUrl = "http://share.rex.me"

            val videoObj = VideoSourceObject()
            videoObj.videoPath = mIntent.getParcelableExtra("local_video_uri")

            val message = WeiboMultiMessage()
            message.textObject = textObj
            message.videoSourceObject = videoObj
            return message
        }



    internal val photoStoryMessage : StoryMessage
        get() {
            val message = StoryMessage()

            getExternalSharePathFileUris(mContext)?.let {
                message.imageUri = getExternalSharePathFileUris(mContext)?.get(0)
            }

            return message

        }


    internal val videoStoryMessage : StoryMessage
        get() {
            val message = StoryMessage()
            message.videoUri = mIntent.getParcelableExtra("local_video_uri")
            return message
        }
}