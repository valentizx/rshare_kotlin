package me.rex.sdk.wechat

import android.graphics.Bitmap
import com.tencent.mm.opensdk.modelmsg.*
import me.rex.sdk.util.bmpToByteArray
import java.security.cert.CertificateNotYetValidException

private const val THUB_SIZE : Int = 150

internal class RWechatHelper {

    private var mReq : SendMessageToWX.Req = SendMessageToWX.Req()
    private var mMsg : WXMediaMessage = WXMediaMessage()

    private val mMediaTagName : String = "rex_share_media_tag"
    private val mMessgeExt : String = "rex_share_ext"

    init {

        mMsg?.mediaTagName = mMediaTagName
        mMsg?.messageExt = mMessgeExt
    }

    internal fun getTextMessageReq(text : String,
                                   scene: RWechatManager.TargetScene) : SendMessageToWX
     .Req {

        val obj : WXTextObject = WXTextObject()
        obj.text = text

        mMsg.mediaObject = obj
        mMsg.description = text

        mReq.transaction = buildAction("text")
        mReq.message = mMsg
        mReq.scene = getScene(scene)

        return mReq
    }

    internal fun getImageMessageReq(image : Bitmap, scene: RWechatManager.TargetScene)
            : SendMessageToWX.Req {

        val thumbBmp = Bitmap.createScaledBitmap(image, THUB_SIZE, THUB_SIZE, true)

        mMsg.mediaObject = getImageObj(image)
        mMsg.thumbData = bmpToByteArray(thumbBmp, false)

        mReq.transaction = buildAction("image")
        mReq.message = mMsg
        mReq.scene = getScene(scene)
        return mReq
    }

    internal fun getWebMessageReq(webpageUrl : String,
                                  title : String?,
                                  description : String?,
                                  thumbImage : Bitmap?,
                                  scene: RWechatManager.TargetScene) : SendMessageToWX.Req {

        mMsg.mediaObject = getWebpageUrlObj(webpageUrl)
        mMsg.title = title
        mMsg.description = description
        thumbImage?.let {
            val thumbBmp = Bitmap.createScaledBitmap(thumbImage, THUB_SIZE, THUB_SIZE, true)
            mMsg.thumbData = bmpToByteArray(thumbBmp, false)

        }

        mReq.transaction = buildAction("web")
        mReq.message = mMsg
        mReq.scene = getScene(scene)
        return mReq
    }


    internal fun getMusicMessageReq(streamUrl : String,
                                    title : String?,
                                    description: String?,
                                    thumbImage: Bitmap?,
                                    webpageUrl: String,
                                    scene: RWechatManager.TargetScene) : SendMessageToWX.Req {

        mMsg.mediaObject = getMusicObj(streamUrl, webpageUrl)
        mMsg.title = title
        mMsg.description = description
        thumbImage?.let {
            val thumbBmp = Bitmap.createScaledBitmap(thumbImage, THUB_SIZE, THUB_SIZE, true)
            mMsg.thumbData = bmpToByteArray(thumbBmp, false)

        }

        mReq.transaction = buildAction("music")
        mReq.message = mMsg
        mReq.scene = getScene(scene)
        return mReq


    }

    internal fun getVideoMessageReq(videoUrl : String,
                                    title : String?,
                                    description: String?,
                                    thumbImage: Bitmap?,
                                    scene: RWechatManager.TargetScene) : SendMessageToWX.Req {

        mMsg.mediaObject = getVideoObj(videoUrl)
        mMsg.title = title
        mMsg.description = description
        thumbImage?.let {
            val thumbBmp = Bitmap.createScaledBitmap(thumbImage, THUB_SIZE, THUB_SIZE, true)
            mMsg.thumbData = bmpToByteArray(thumbBmp, false)

        }

        mReq.transaction = buildAction("music")
        mReq.message = mMsg
        mReq.scene = getScene(scene)
        return mReq


    }

    internal fun getMiniProgramMessageReq(userName: String,
                                          path: String,
                                          type: Int,
                                          webpageUrl: String,
                                          tile: String?,
                                          description: String?,
                                          thumbImage: Bitmap?,
                                          scene: RWechatManager.TargetScene) : SendMessageToWX.Req {

        mMsg.mediaObject = getMiniProgramObj(webpageUrl, userName, path, type)
        mMsg.title = tile
        mMsg.description = description

        thumbImage?.let {
            val thumbBmp = Bitmap.createScaledBitmap(thumbImage, THUB_SIZE, THUB_SIZE, true)
            mMsg.thumbData = bmpToByteArray(thumbBmp, false)

        }

        mReq.transaction = buildAction("miniprogram")
        mReq.message = mMsg
        mReq.scene = getScene(scene)
        return mReq



    }

    internal fun getFileMessageReq(path: String,
                                   title: String?,
                                   thumbImage: Bitmap?,
                                   scene: RWechatManager.TargetScene) : SendMessageToWX.Req {

        mMsg.mediaObject = getFileObj(path)
        mMsg.title = title

        thumbImage?.let {
            val thumbBmp = Bitmap.createScaledBitmap(thumbImage, THUB_SIZE, THUB_SIZE, true)
            mMsg.thumbData = bmpToByteArray(thumbBmp, false)
        }
        mReq.transaction = buildAction("file")
        mReq.message = mMsg
        mReq.scene = getScene(scene)
        return mReq
    }

     /**
      * ===================================
      * */
    private fun getImageObj(image : Bitmap) : WXImageObject {
         val obj = WXImageObject(image)
         return obj
    }

    private fun getWebpageUrlObj(webpageUrl : String) : WXWebpageObject {
         val obj : WXWebpageObject = WXWebpageObject()
         obj.webpageUrl = webpageUrl
         return obj
     }
    private fun getMusicObj(streamUrl : String, webpageUrl: String) : WXMusicObject {
         val obj : WXMusicObject = WXMusicObject()
         obj.musicUrl = webpageUrl
         obj.musicDataUrl = streamUrl
         return obj

     }

    private fun getMiniProgramObj(webapgeUrl : String,
                                    userName : String,
                                    path : String,
                                    type : Int) : WXMiniProgramObject {

         val obj : WXMiniProgramObject = WXMiniProgramObject()
         obj.webpageUrl = webapgeUrl
         obj.userName = userName
         obj.path = path
         obj.miniprogramType = type
         return obj

     }

    private fun getVideoObj(videoUrl: String) : WXVideoObject {
         val obj : WXVideoObject = WXVideoObject()
         obj.videoUrl = videoUrl
         return obj
     }

    private fun getFileObj(path: String) : WXFileObject {

        val obj = WXFileObject()
        obj.setContentLengthLimit(1024 * 1024 * 10)
        obj.setFilePath(path)
        return obj
    }


     /**
      *=====================================
      * **/
    private fun getScene(targetScene: RWechatManager.TargetScene) : Int {
        when (targetScene) {
            RWechatManager.TargetScene.Session -> {
                return SendMessageToWX.Req.WXSceneSession
            }
            RWechatManager.TargetScene.Favorite -> {
                return SendMessageToWX.Req.WXSceneFavorite
            }
            RWechatManager.TargetScene.Timeline -> {
                return SendMessageToWX.Req.WXSceneTimeline
            }
            else -> return -1
        }
    }

    private fun buildAction(flag : String) : String {
        return flag + System.currentTimeMillis()
    }


}