package me.rex.sdk.facebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.model.ShareVideoContent
import com.facebook.share.widget.ShareDialog
import me.rex.sdk.RSharePlatform
import me.rex.sdk.ShareContentType
import me.rex.sdk.ShareState
import me.rex.sdk.util.deleteExternalShareDir

private const val TAG = "RFacebookActivity"
class RFacebookActivity : Activity() {


    private val mCbm = CallbackManager.Factory.create()
    private val mSd = ShareDialog(this)
    private val mCb : FacebookCallback<*> = object : FacebookCallback<Any> {
        override fun onSuccess(o: Any) {

            RFacebookManager.instance.shareCallback?.let {
                RFacebookManager.instance.shareCallback?.invoke(RSharePlatform.Facebook,
                        ShareState.Success, null)
            }
            deleteExternalShareDir(RFacebookManager.instance.context)
            finish()
        }

        override fun onCancel() {
            RFacebookManager.instance.shareCallback?.let {
                RFacebookManager.instance.shareCallback?.invoke(RSharePlatform.Facebook,
                        ShareState.Cancel, null)
            }

            deleteExternalShareDir(RFacebookManager.instance.context)
            finish()

        }

        override fun onError(error: FacebookException) {
            RFacebookManager.instance.shareCallback?.let {
                RFacebookManager.instance.shareCallback?.invoke(RSharePlatform.Facebook,
                        ShareState.Failure, error.toString())
            }
            deleteExternalShareDir(RFacebookManager.instance.context)
            finish()

        }
    }
    private lateinit var mHelper : RFacebookHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent()

    }
    private fun handleIntent() {
        val intent = intent
        mHelper = RFacebookHelper(intent, RFacebookManager.instance.context)
        val type = intent.getSerializableExtra("type")

        when (type) {
            ShareContentType.Photo -> {
                val sb = StrictMode.VmPolicy.Builder()
                StrictMode.setVmPolicy(sb.build())
                sb.detectFileUriExposure()
                sharePhoto(mHelper.photoContent, ShareDialog.Mode.NATIVE)
            }
            ShareContentType.Video -> shareVideo(mHelper.videoContent, ShareDialog.Mode.NATIVE)
            ShareContentType.Webpage -> shareWebpage(mHelper.linkContent, mHelper.mode)
        }

    }


    private fun shareWebpage(content : ShareLinkContent, mode : ShareDialog.Mode) {
        mSd.registerCallback(mCbm, mCb as FacebookCallback<Sharer.Result>)
        if (mSd.canShow(content, mode)) {
            mSd.show(content, mode)
        }
    }

    private fun sharePhoto(content: SharePhotoContent, mode : ShareDialog.Mode) {
        mSd.registerCallback(mCbm, mCb as FacebookCallback<Sharer.Result>)
        if (mSd.canShow(content, mode)) {
            mSd.show(content, mode)
        }
    }
    private fun shareVideo(content: ShareVideoContent, mode: ShareDialog.Mode) {
        mSd.registerCallback(mCbm, mCb as FacebookCallback<Sharer.Result>)
        if (mSd.canShow(content, mode)) {
            mSd.show(content, mode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCbm.onActivityResult(requestCode, resultCode, data)
        finish()
    }
}