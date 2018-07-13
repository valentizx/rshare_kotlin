package me.rex.sdk.qq

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import me.rex.sdk.RSharePlatform
import me.rex.sdk.ShareContentType
import me.rex.sdk.ShareState
import me.rex.sdk.util.deleteExternalShareDir
import me.rex.sdk.util.getQQAppId
import me.rex.sdk.util.mainHandler

class RQqActivity : Activity() {

    private lateinit var mHelper : RQqHelper
    private val mShareCallback = RQqManager.instance.shareCallback

    private lateinit var mTencent : Tencent

    private val mQqShareListener = object : IUiListener {
        override fun onComplete(o: Any) {

            mShareCallback?.let {
                mShareCallback.invoke(RSharePlatform.QQ, ShareState.Success, null)
            }
            finish()
        }

        override fun onError(uiError: UiError) {
            mShareCallback?.let {
                mShareCallback.invoke(RSharePlatform.QQ, ShareState.Failure, uiError.errorMessage)
            }
            finish()
        }

        override fun onCancel() {
            mShareCallback?.let {
                mShareCallback.invoke(RSharePlatform.QQ, ShareState.Cancel, null)
            }
            finish()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mHelper = RQqHelper(intent, RQqManager.instance.context)
        handleIntent()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_QQ_SHARE) Tencent.onActivityResultData(requestCode, resultCode, data, mQqShareListener)
    }

    override fun onDestroy() {
        super.onDestroy()

        mTencent.releaseResource()
        deleteExternalShareDir(RQqManager.instance.context)

    }


    private fun handleIntent(){
        val type = intent.getSerializableExtra("type") as ShareContentType
        when (type) {
            ShareContentType.Webpage -> share(mHelper.webpageParams)
            ShareContentType.Photo -> share(mHelper.imageParams)
            ShareContentType.App -> share(mHelper.appParams)
            ShareContentType.Music -> share(mHelper.musicParams)

        }


    }
    private fun sdkInitialize() {
        val appId = getQQAppId(RQqManager.instance.context)
        mTencent = Tencent.createInstance(appId, RQqManager.instance.context)
    }

    private fun share (params : Bundle) {
        sdkInitialize()

        mainHandler.post(
                Runnable {
                    kotlin.run {
                        mTencent.shareToQQ(this@RQqActivity, params, mQqShareListener)
                    }
                }
        )
    }


}