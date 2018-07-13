package me.rex.sdk.twitter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.twitter.sdk.android.tweetcomposer.TweetUploadService
import me.rex.sdk.RSharePlatform
import me.rex.sdk.ShareState
import me.rex.sdk.util.deleteExternalShareDir

class RTwitterTweetResultReciver : BroadcastReceiver() {

    private val mShareCallback = RTwitterManager.instance.shareCallback
    private val mContext = RTwitterManager.instance.context

    override fun onReceive(context: Context?, intent: Intent?) {
        mShareCallback.let {
            when (intent?.action) {
                TweetUploadService.UPLOAD_SUCCESS -> {
                    mShareCallback?.invoke(RSharePlatform.Twitter, ShareState.Success, null)
                }
                TweetUploadService.TWEET_COMPOSE_CANCEL -> {
                    mShareCallback?.invoke(RSharePlatform.Twitter, ShareState.Cancel, null)
                }
                TweetUploadService.UPLOAD_FAILURE -> {
                    mShareCallback?.invoke(RSharePlatform.Twitter, ShareState.Failure, "发推失败")
                }
                else -> {}
            }
        }
        deleteExternalShareDir(mContext!!)

    }

}