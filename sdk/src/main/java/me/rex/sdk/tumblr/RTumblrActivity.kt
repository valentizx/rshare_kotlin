package me.rex.sdk.tumblr

import android.app.Activity
import android.os.Bundle
import com.flurry.android.FlurryAgent
import com.flurry.android.tumblr.Post
import com.flurry.android.tumblr.PostListener
import com.flurry.android.tumblr.TumblrShare
import me.rex.sdk.share.RShareCallback
import me.rex.sdk.share.RSharePlatform
import me.rex.sdk.share.ShareContentType
import me.rex.sdk.share.ShareState
import me.rex.sdk.util.getTumblrConsumerKey
import me.rex.sdk.util.getTumblrConsumerSecret
import me.rex.sdk.util.getTumblrFlurryKey

private const val TAG = "RTumblrActivity==>"

class RTumblrActivity : Activity() {

    private var mShareCallback : RShareCallback? = RTumblrManager.instance.shareCallback

    private val mPosterListener : PostListener = object  : PostListener {
        override fun onPostFailure(p0: String?) {
            mShareCallback?.let {
                mShareCallback?.invoke(RSharePlatform.Tumblr, ShareState.Failure, p0)
            }
            finish()
        }

        override fun onPostSuccess(p0: Long?) {
            mShareCallback?.let{
                mShareCallback?.invoke(RSharePlatform.Tumblr, ShareState.Success, null)
            }
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent()
    }

    private fun handleIntent() {
        val helper = RTumblrHelper(intent)
        val type = intent.getSerializableExtra("type") as ShareContentType
        when (type) {
            ShareContentType.Photo -> post(helper.imageParam)
            ShareContentType.Text -> post(helper.textParam)
        }
    }

    private fun post(param : Post) {
        sdkInitialize()
        param.setPostListener(mPosterListener)
        TumblrShare.post(this, param)
    }
    private fun sdkInitialize() {
        val mContext = RTumblrManager.instance.context
        val key = getTumblrConsumerKey(mContext )
        val secret = getTumblrConsumerSecret(mContext)
        val flurryKey = getTumblrFlurryKey(mContext)
        FlurryAgent.setLogEnabled(true)

        FlurryAgent.init(this, flurryKey)

        TumblrShare.setOAuthConfig(key, secret)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mShareCallback?.let {
            mShareCallback?.invoke(RSharePlatform.Tumblr, ShareState.Cancel, null)
        }
    }
}