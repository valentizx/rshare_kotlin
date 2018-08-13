package me.rex.sdk.facebook

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.facebook.share.model.*
import com.facebook.share.widget.ShareDialog
import me.rex.sdk.share.Mode
import me.rex.sdk.util.getExternalSharePathBitmaps


internal class RFacebookHelper (val intent : Intent, val context: Context) {


    private val mIntent = intent
    private val mContext = context


    internal val mode : ShareDialog.Mode
        get() {
        val tmpMode : Mode = mIntent.getSerializableExtra("mode") as Mode
        when (tmpMode) {
            Mode.Automatic -> return ShareDialog.Mode.AUTOMATIC
            Mode.Feed -> return ShareDialog.Mode.FEED
            Mode.Web -> return ShareDialog.Mode.WEB
            else -> return ShareDialog.Mode.NATIVE
        }
    }

    internal var linkContent : ShareLinkContent = ShareLinkContent.Builder().build()
        get() {
            val hashTag = ShareHashtag.Builder()
                   .setHashtag(mIntent.getStringExtra("hashTag"))
                   .build()
            val linkContent = ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(mIntent.getStringExtra("webpageUrl")))
                    .setShareHashtag(hashTag)
                    .setQuote(mIntent.getStringExtra("quote"))
                    .build()
            return linkContent
        }
        private set

    internal var photoContent : SharePhotoContent = SharePhotoContent.Builder().build()
        get() {
            val bitmaps = getExternalSharePathBitmaps(mContext)
            var photos = ArrayList<SharePhoto>()

            bitmaps?.let {
                for (i in 0 until  bitmaps.size) {
                    val photo = SharePhoto.Builder()
                            .setBitmap(bitmaps.get(i))
                            .build()
                    photos.add(photo)
                }
            }

            val photoContent = SharePhotoContent.Builder()
                    .addPhotos(photos)
                    .build()
            return photoContent



        }

    internal var videoContent : ShareVideoContent = ShareVideoContent.Builder().build()
        get() {
            val videoUri : Uri = mIntent.getParcelableExtra("localVideoPath")
            val video = ShareVideo.Builder()
                    .setLocalUrl(videoUri)
                    .build()
            val videoContent = ShareVideoContent.Builder()
                    .setVideo(video)
                    .build()
            return videoContent

        }


}