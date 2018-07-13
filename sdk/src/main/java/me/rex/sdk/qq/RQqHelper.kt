package me.rex.sdk.qq

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QzonePublish
import com.tencent.connect.share.QzoneShare
import me.rex.sdk.util.getExternalSharePathFiles


private const val TAG = "RQqHelper ==>"

class RQqHelper (val intent : Intent, val context: Context) {

    private val mIntent = intent
    private val mContext = context

    internal val webpageParams : Bundle
        get() {
            val params = Bundle()
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
            params.putString(QQShare.SHARE_TO_QQ_TITLE, mIntent.getStringExtra("title"))
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mIntent.getStringExtra("description"))
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mIntent.getStringExtra("webpageUrl"))
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mIntent.getStringExtra("thumbImage"))

            Log.e(TAG, "构造参数")
            return params
        }


    internal val imageParams : Bundle
        get() {
            val params = Bundle()
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE)

            getExternalSharePathFiles(mContext)?.let {
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, getExternalSharePathFiles
                (mContext)?.get(0)?.absolutePath)
            }

            return params
        }

    internal val musicParams : Bundle
        get() {
            val params = Bundle()
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO)
            params.putString(QQShare.SHARE_TO_QQ_TITLE, mIntent.getStringExtra("title"))
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mIntent.getStringExtra("description"))
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mIntent.getStringExtra("targetUrl"))
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mIntent.getStringExtra("thumbImage"))
            params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, mIntent.getStringExtra("musicUrl"))
            return params
        }

    internal val appParams : Bundle
        get() {
            val params = Bundle()
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP)
            params.putString(QQShare.SHARE_TO_QQ_TITLE, mIntent.getStringExtra("title"))
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mIntent.getStringExtra("description"))
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mIntent.getStringExtra("targetUrl"))
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mIntent.getStringExtra("thumbImage"))
            return params
        }

    internal val qZoneWebpageParams : Bundle
        get() {
            val params = Bundle()
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare
                    .SHARE_TO_QZONE_TYPE_IMAGE_TEXT)
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mIntent.getStringExtra("title"))
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, mIntent.getStringExtra("description"))
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, mIntent.getStringExtra("webpageUrl"))

            val imgs = mIntent.getStringArrayListExtra("images")
            if (imgs != null && imgs.size != 0) {
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgs)
            }
            return params
        }

    internal val qZoneImagesParams : Bundle
        get() {
            val params = Bundle()
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD)
            params.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, mIntent.getStringExtra("description"))
            val imgs = mIntent.getStringArrayListExtra("images")
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgs)
            return params

        }

    internal val qZoneVideoParams : Bundle
        get() {
            val params = Bundle()
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish
                    .PUBLISH_TO_QZONE_TYPE_PUBLISHVIDEO)
            params.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, mIntent.getStringExtra("description"))

            params.putString(QzonePublish.PUBLISH_TO_QZONE_VIDEO_PATH, mIntent.getStringExtra("local_video_path"))

            return params
        }
}