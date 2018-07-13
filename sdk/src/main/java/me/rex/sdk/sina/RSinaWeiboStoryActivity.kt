package me.rex.sdk.sina

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sina.weibo.sdk.share.WbShareCallback
import com.sina.weibo.sdk.share.WbShareHandler
import me.rex.sdk.RSharePlatform
import me.rex.sdk.ShareContentType
import me.rex.sdk.ShareState
import me.rex.sdk.util.deleteExternalShareDir

class RSinaWeiboStoryActivity : Activity(), WbShareCallback {


    private val shareCallback = RSinaWeiboManager.instance.shareCallback

    private lateinit var mHandler : WbShareHandler
    private lateinit var mContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mHandler = WbShareHandler(this)
        mHandler.registerApp()
        handleIntent()
    }

    private fun handleIntent() {
        val intent = intent
        val type = intent.getSerializableExtra("type") as ShareContentType

        mContext = RSinaWeiboManager.instance.context
        val helper = RSinaWeiboHelper(intent, mContext)


        when (type) {

            ShareContentType.Photo -> {
                mHandler.shareToStory(helper.photoStoryMessage)
            }

            ShareContentType.Video -> {
                mHandler.shareToStory(helper.videoStoryMessage)
            }
        }
    }
    override fun onWbShareCancel() {

        shareCallback?.let {
            shareCallback.invoke(RSharePlatform.Sina, ShareState.Cancel, null)
        }
        shareFinish()

    }

    override fun onWbShareSuccess() {

        shareCallback?.let {
            shareCallback.invoke(RSharePlatform.Sina, ShareState.Success, null)
        }
        shareFinish()

    }

    override fun onWbShareFail() {
        shareCallback?.let {
            shareCallback.invoke(RSharePlatform.Sina, ShareState.Failure, "分享失败")
        }
        shareFinish()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        mHandler.doResultIntent(intent, this)


    }


    private fun shareFinish() {

        deleteExternalShareDir(mContext)
        finish()

    }
}