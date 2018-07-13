package me.rex.sdk.wechat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import me.rex.sdk.RShare
import me.rex.sdk.RShareCallback
import me.rex.sdk.RSharePlatform
import me.rex.sdk.ShareState
import me.rex.sdk.util.getPath
import me.rex.sdk.util.getWechatAppId
import me.rex.sdk.util.isInstalled


const val MINIPROGRAM_TYPE_RELEASE = 0
const val MINIPROGRAM_TYPE_TEST = 1
const val MINIPROGRAM_TYPE_PREVIEW = 2

private const val TAG = "RWechatManager==>"

class RWechatManager private constructor() : RShare() {


    companion object {
        val instance : RWechatManager get() = Inner.instance
    }
    private object Inner {
        val instance = RWechatManager()
    }

    internal lateinit var mIWXApi : IWXAPI

    internal var shareCallback : RShareCallback? = null
        private set

    private val mHelper : RWechatHelper = RWechatHelper()

    internal fun sdkInitialize(context : Context) {

        val appId = getWechatAppId(context)

        appId?.let {
            mIWXApi = WXAPIFactory.createWXAPI(context, appId)
            mIWXApi.registerApp(appId)
        }
    }

    internal fun unregisterSdk() {
        mIWXApi.unregisterApp()
        shareCallback = null
    }

    fun shareText(context: Context,
                  text : String,
                  scene: TargetScene,
                  callback : RShareCallback?) : Unit{

        if (!isInstalled(context, RSharePlatform.WeChat)) {
            Log.e(TAG, "微信未安装")
            return
        }

        sdkInitialize(context)

        shareCallback = callback

        val req = mHelper.getTextMessageReq(text, scene)
        mIWXApi.sendReq(req)

    }


    fun shareImage(context: Context,
                   image : Bitmap,
                   scene: TargetScene,
                   callback: RShareCallback?) {
        if (!isInstalled(context, RSharePlatform.WeChat)) {
            Log.e(TAG, "微信未安装")
            return
        }

        sdkInitialize(context)
        shareCallback = callback

        val req = mHelper.getImageMessageReq(image, scene)
        mIWXApi.sendReq(req)

    }

    fun shareWebpage(context: Context,
                    webpageUrl : String,
                    title : String?,
                    description : String?,
                    thumbImage : Bitmap?,
                    scene: TargetScene,
                    callback: RShareCallback?) {
        if (!isInstalled(context, RSharePlatform.WeChat)) {
            Log.e(TAG, "微信未安装")
            return
        }

        sdkInitialize(context)
        shareCallback = callback

        val req = mHelper.getWebMessageReq(webpageUrl, title, description, thumbImage, scene)
        mIWXApi.sendReq(req)
    }

    fun shareMusic(context: Context,
                   musicUrl : String,
                   title: String?,
                   description: String?,
                   thumbImage: Bitmap?,
                   webpageUrl: String,
                   scene: TargetScene,
                   callback: RShareCallback?) {
        if (!isInstalled(context, RSharePlatform.WeChat)) {
            Log.e(TAG, "微信未安装")
            return
        }

        sdkInitialize(context)
        shareCallback = callback

        val req = mHelper.getMusicMessageReq(musicUrl, title, description, thumbImage, webpageUrl,
                scene)
        mIWXApi.sendReq(req)

    }

    fun shareVideo(context: Context,
                   videoUrl : String,
                   title: String?,
                   description: String?,
                   thumbImage: Bitmap?,
                   scene: TargetScene,
                   callback: RShareCallback?) {
        if (!isInstalled(context, RSharePlatform.WeChat)) {
            Log.e(TAG, "微信未安装")
            return
        }

        sdkInitialize(context)
        shareCallback = callback

        val req = mHelper.getVideoMessageReq(videoUrl, title, description, thumbImage, scene)
        mIWXApi.sendReq(req)
    }


    fun shareMiniProgram(context: Context,
                         userName : String,
                         path : String,
                         type : Int,
                         webpageUrl: String,
                         title: String?,
                         description: String?,
                         thumbImage: Bitmap?,
                         scene: TargetScene,
                         callback: RShareCallback?) {
        if (!isInstalled(context, RSharePlatform.WeChat)) {
            Log.e(TAG, "微信未安装")
            return
        }

        sdkInitialize(context)
        shareCallback = callback

        val req = mHelper.getMiniProgramMessageReq(userName, path, type, webpageUrl, title,
                description, thumbImage, scene)

        mIWXApi.sendReq(req)

    }

    fun shareFile(context: Context,
                  fileUri : Uri,
                  title : String?,
                  thumbImage: Bitmap?,
                  scene: TargetScene,
                  callback: RShareCallback?) {
        if (!isInstalled(context, RSharePlatform.WeChat)) {
            Log.e(TAG, "微信未安装")
            return
        }

        sdkInitialize(context)
        shareCallback = callback

        val path = getPath(context, fileUri)

        path?.let {
            var req = mHelper.getFileMessageReq(path, title, thumbImage, scene)
            mIWXApi.sendReq(req)
        }

    }


    fun handleIntent(context : IWXAPIEventHandler, intent: Intent) : Boolean {
        return mIWXApi.handleIntent(intent, context)
    }

    fun onResp(context : Activity, baseResp : BaseResp) {
        when (baseResp?.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                shareCallback?.invoke(RSharePlatform.WeChat, ShareState.Success, null)

            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                shareCallback?.invoke(RSharePlatform.WeChat, ShareState.Cancel, null)
            }
            BaseResp.ErrCode.ERR_BAN -> {
                shareCallback?.invoke(RSharePlatform.WeChat, ShareState.Failure, "请求被拒绝")
            }
            BaseResp.ErrCode.ERR_UNSUPPORT -> {
                shareCallback?.invoke(RSharePlatform.WeChat, ShareState.Failure, "不支持该操作")
            }
            BaseResp.ErrCode.ERR_SENT_FAILED -> {
                shareCallback?.invoke(RSharePlatform.WeChat, ShareState.Failure, "发送失败")
            }
        }
        unregisterSdk()
        context.finish()

    }
    fun onReq(context: Context, baseReq: BaseReq) {}




    enum class TargetScene {

        Favorite, /**收藏 */

        Session, /**好友 */

        Timeline /**朋友圈*/
    }



}