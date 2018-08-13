package me.rex.sdk.share

import android.animation.TimeAnimator
import android.content.Context
import android.graphics.Bitmap
import com.tencent.mm.opensdk.utils.Log
import me.rex.sdk.facebook.RFacebookManager
import me.rex.sdk.googleplus.RGooglePlusManager
import me.rex.sdk.instagram.RInstagramManager
import me.rex.sdk.line.RLineManager
import me.rex.sdk.pinterest.RPinterestManager
import me.rex.sdk.qq.RQqManager
import me.rex.sdk.sina.RSinaWeiboManager
import me.rex.sdk.tumblr.RTumblrManager
import me.rex.sdk.twitter.RTwitterManager
import me.rex.sdk.wechat.RWechatManager
import me.rex.sdk.whatsapp.RWhatsAppManager

enum class ShareChannel {
    QQSession,
    QQFavorite,
    QQDataLine,
    QZone,
    WechatSession,
    WechatFavorite,
    WechatTimeline,
    FacebookClient,
    FacebookBroswer,
    TwitterInnerApp,
    TwitterClient,
    SinaWeibo,
    SinaWeiboStory,
    Line,
    InstagramSystem,
    InstagramClient,
    Tumblr,
    Pinterest,
    GooglePlus,
    WhatsApp
}
private const val TAG : String = "RShareManager"
class RShareManager private constructor() {

    companion object {
        val instance : RShareManager get() = Inner.instance
    }
    private object Inner {
        val instance = RShareManager()
    }

    fun shareText(context: Context,
                  content: RTextContent,
                  channel : ShareChannel,
                  callback: RShareCallback?) {
        when (channel) {
            ShareChannel.WechatSession -> RWechatManager.instance.shareText(context, content
                    .body, RWechatManager.TargetScene.Session, callback)
            ShareChannel.WechatFavorite -> RWechatManager.instance.shareText(context, content
                    .body, RWechatManager.TargetScene.Favorite, callback)
            ShareChannel.WechatTimeline -> RWechatManager.instance.shareText(context, content
                    .body, RWechatManager.TargetScene.Timeline, callback)
            ShareChannel.TwitterClient -> RTwitterManager.instance.share(context, null, content
                    .body, null, null, Mode.Native, callback)
            ShareChannel.TwitterInnerApp -> RTwitterManager.instance.share(context, null, content
                    .body, null, null, Mode.Automatic, callback)
            ShareChannel.SinaWeibo -> RSinaWeiboManager.instance.shareText(context, content.body,
                    callback)
            ShareChannel.Line -> RLineManager.instance.share(context, content.body)
            ShareChannel.Tumblr -> RTumblrManager.instance.shareText(context, content.body,
                    content.title, content.webpageUrl, callback)
            ShareChannel.WhatsApp -> RWhatsAppManager.instance.share(context, null, content.body)
            ShareChannel.GooglePlus -> RGooglePlusManager.instance.share(context, content
                    .webpageUrl, content.body)
            else -> Log.e(TAG, "该种方式不支持文字分享")
        }

    }
    fun shareVideo(context: Context,
                   content: RVideoContent,
                   channel : ShareChannel,
                   callback: RShareCallback?) {
        when(channel) {
            ShareChannel.QZone -> RQqManager.instance.publishVideoToZone(context, content
                    .localVideoUrl, content.quote, callback)
            ShareChannel.WechatSession -> RWechatManager.instance.shareFile(context, content
                    .localVideoUrl, content.title, content.thumbImage, RWechatManager.TargetScene
                    .Session, callback)
            ShareChannel.WechatFavorite -> RWechatManager.instance.shareFile(context, content
                    .localVideoUrl, content.title, content.thumbImage, RWechatManager.TargetScene
                    .Favorite, callback)
            ShareChannel.FacebookClient -> RFacebookManager.instance.shareLocalVideo(context,
                    content.localVideoUrl)
            ShareChannel.SinaWeibo -> RSinaWeiboManager.instance.shareLocalVideo(context, content
                    .localVideoUrl, content.quote, false, callback)
            ShareChannel.SinaWeiboStory -> RSinaWeiboManager.instance.shareLocalVideo(context, content
                    .localVideoUrl, content.quote, true, callback)

            ShareChannel.InstagramSystem -> RInstagramManager.instance.shareVideo(context,
                    content.localVideoUrl)
            else -> Log.e(TAG, "该种方式不支持视频分享")
        }

    }
    fun shareImage(context: Context,
                   content: RImageContent,
                   channel : ShareChannel,
                   callback: RShareCallback?) {
        var img : ArrayList<Bitmap> = ArrayList<Bitmap>().apply {
            add(content.image)
        }

        when (channel) {
            ShareChannel.QQSession, ShareChannel.QQDataLine, ShareChannel.QQFavorite ->
                    RQqManager.instance.shareImage(context, content.image, callback)
            ShareChannel.QZone -> RQqManager.instance.publishImagesToZone(context, img, content
                    .quote, callback)
            ShareChannel.WechatSession -> RWechatManager.instance.shareImage(context, content
                    .image, RWechatManager.TargetScene.Session, callback)
            ShareChannel.WechatFavorite -> RWechatManager.instance.shareImage(context, content
                    .image, RWechatManager.TargetScene.Favorite, callback)

            ShareChannel.WechatTimeline -> RWechatManager.instance.shareImage(context, content
                    .image, RWechatManager.TargetScene.Timeline, callback)
            ShareChannel.FacebookClient -> RFacebookManager.instance.sharePhoto(context, img)
            ShareChannel.TwitterInnerApp -> RTwitterManager.instance.share(context, null,
                    content.quote, content.image, null , Mode.Automatic, callback)
            ShareChannel.TwitterClient -> RTwitterManager.instance.share(context, null,
                    content.quote, content.image, null , Mode.Native, callback)
            ShareChannel.SinaWeibo -> RSinaWeiboManager.instance.sharePhoto(context, img, content
                    .quote, false, callback)
            ShareChannel.SinaWeiboStory -> RSinaWeiboManager.instance.sharePhoto(context, img, content
                    .quote, true, callback)
            ShareChannel.Line -> RLineManager.instance.share(context, content.image)
            ShareChannel.InstagramClient -> RInstagramManager.instance.shareImage(context,
                    content.image, Mode.Automatic)
            ShareChannel.InstagramSystem -> RInstagramManager.instance.shareImage(context,
                    content.image, Mode.System)
            ShareChannel.Tumblr -> if (!content.imageUrl.isNullOrBlank())  RTumblrManager.instance.shareImage(context, content.imageUrl, content.quote,
                    content.webpageUrl, callback) else print("参数不正确")
            ShareChannel.Pinterest -> if (!content.imageUrl.isNullOrBlank()) RPinterestManager
                    .instance.shareImage(context, content.imageUrl) else Log.e(TAG, "该种方式不支持图片分享")
            ShareChannel.WhatsApp -> RWhatsAppManager.instance.share(context, content.image,
                    content.quote)
            else -> Log.e(TAG, "该种方式不支持图片分享")
        }
    }
    fun shareWebpage(context: Context,
                     content: RWebpageContent,
                     channel : ShareChannel,
                     callback: RShareCallback?) {

        when (channel) {
            ShareChannel.QQFavorite, ShareChannel.QQDataLine, ShareChannel.QQSession ->
                RQqManager.instance.shareWebpage(context, content.webpageUrl, content.title,
                        content.quote, content.thumbImageUrl, callback)
            ShareChannel.QZone -> {
                val img = ArrayList<String>().apply {
                    add(content.thumbImageUrl)
                }
                RQqManager.instance.shareWebpageToZone(context, content.webpageUrl, content
                        .title, content.quote, img, callback)
            }
            ShareChannel.WechatSession -> RWechatManager.instance.shareWebpage(context, content
                    .webpageUrl, content.title, content.quote, content.thumbImage, RWechatManager
                    .TargetScene.Session, callback)
            ShareChannel.WechatFavorite -> RWechatManager.instance.shareWebpage(context, content
                    .webpageUrl, content.title, content.quote, content.thumbImage, RWechatManager
                    .TargetScene.Favorite, callback)
            ShareChannel.WechatTimeline -> RWechatManager.instance.shareWebpage(context, content
                    .webpageUrl, content.title, content.quote, content.thumbImage, RWechatManager
                    .TargetScene.Timeline, callback)
            ShareChannel.FacebookClient -> RFacebookManager.instance.shareWebpage(context,
                    content.webpageUrl, content.quote, content.hashTag, Mode.Automatic, callback)
            ShareChannel.FacebookBroswer -> RFacebookManager.instance.shareWebpage(context,
                    content.webpageUrl, content.quote, content.hashTag, Mode.Feed, callback)
            ShareChannel.TwitterInnerApp -> RTwitterManager.instance.share(context, content
                    .webpageUrl, content.quote, content.thumbImage, content.hashTag, Mode
                    .Automatic, callback)
            ShareChannel.TwitterClient -> RTwitterManager.instance.share(context, content
                    .webpageUrl, content.quote, content.thumbImage, content.hashTag, Mode
                    .Native, callback)
            ShareChannel.SinaWeibo -> RSinaWeiboManager.instance.shareWebpage(context, content
                    .webpageUrl, content.title, content.quote, content.thumbImage, callback)
            ShareChannel.Tumblr -> if (!content.thumbImageUrl.isNullOrBlank()) RTumblrManager
                    .instance.shareImage(context, content.thumbImageUrl, content.quote, content
                    .webpageUrl, callback) else Log.e(TAG, "参数错误")
            ShareChannel.WhatsApp -> RWhatsAppManager.instance.share(context,content.thumbImage,
                    content.quote)
            ShareChannel.GooglePlus -> RGooglePlusManager.instance.share(context, content
                    .webpageUrl, content.quote)
            else -> Log.e(TAG, "该种方式不支持网页分享")
        }

    }
}