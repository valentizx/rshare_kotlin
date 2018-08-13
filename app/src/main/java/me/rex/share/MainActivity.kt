package me.rex.share

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.tencent.mm.opensdk.utils.Log
import me.rex.sdk.share.Mode
import me.rex.sdk.share.RShareCallback
import me.rex.sdk.facebook.RFacebookManager
import me.rex.sdk.googleplus.RGooglePlusManager
import me.rex.sdk.instagram.RInstagramManager
import me.rex.sdk.line.RLineManager
import me.rex.sdk.pinterest.RPinterestManager
import me.rex.sdk.qq.RQqManager
import me.rex.sdk.share.RImageContent
import me.rex.sdk.sina.RSinaWeiboManager
import me.rex.sdk.tumblr.RTumblrManager
import me.rex.sdk.twitter.RTwitterManager
import me.rex.sdk.wechat.MINIPROGRAM_TYPE_RELEASE
import me.rex.sdk.wechat.RWechatManager
import me.rex.sdk.whatsapp.RWhatsAppManager
import java.util.*

private const val Facebook = "FACEBOOK"
private const val Instagram = "INSTAGRAM"
private const val QZone = "QZONE"
private const val Sina = "SINA"
private const val Wechat = "WX"

private const val PICK_VIDEO_REQUEST_CODE = 10069
private const val PICK_FILE_REQUEST_CODE = 10070

class MainActivity : AppCompatActivity() {


    private val TAG : String = "MainActivity===>"
    private var flag : String = ""

    private lateinit var mPhoto : Bitmap
    private val mThumbImageUrl = "https://gss0.bdstatic.com/-4o3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=6fe05760811001e94e3c130980351cd1/cefc1e178a82b90186bcddba7f8da9773912ef22.jpg"


    private val mWebpageUrl : String =  "https://www.nytimes.com/2018/05/04/arts/music/playlist-christina-aguilera-kanye-west-travis-scott-dirty-projectors.html"
    private val mDescription : String = "流行天后 Christina Aguilera 时隔六年回归全新录音室专辑「Liberation」于 2018 年 6 月 15 日发布."



    private var mShareCallback : RShareCallback = { platform, state, errorInfo ->

        Log.e(TAG, platform.toString())
        Log.e(TAG, state.toString())
        Log.e(TAG, errorInfo ?: "")

    }

    private val mTitle : String = "Liberation"
    private val mHashTag : String = "#liberation"
    private val mVideoUrl : String = "https://www.youtube.com/watch?v=DSRSgMp5X1w"
    private val mMusicUrl : String = "http://url.cn/5tZF9KT"
    private val mAppUrl : String = "http://a.app.qq.com/o/simple.jsp?pkgname=com.tencent.mobileqq&from=singlemessage"
    private val mAudioStreamUrl : String = "http://10.136.9.109/fcgi-bin/fcg_music_get_playurl.fcg?song_id=1234&redirect=0&filetype=mp3&qqmusic_fromtag=15&app_id=100311325&app_key=b233c8c2c8a0fbee4f83781b4a04c595&device_id=1234"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPhoto = BitmapFactory.decodeResource(resources, R.drawable.c_1)

        Log.e("散列",RFacebookManager.instance.printKeyHash(this))

    }

    fun share(view : View) {
        val button : Button = view as Button

        when (button.id) {

            R.id.fb_wb_btn -> {
                RFacebookManager.instance.shareWebpage(this, mWebpageUrl, mDescription,
                        mHashTag, Mode.Native, mShareCallback)
            }
            R.id.fb_ph_btn -> {
                val photos = ArrayList<Bitmap>()
                photos.add(mPhoto)
                photos.add(mPhoto)
                RFacebookManager.instance.sharePhoto(this, photos)
            }
            R.id.fb_vd_btn -> {
                flag = Facebook
                chooseVideo()

            }
            R.id.tw_app_btn -> {
                RTwitterManager.instance.share(this, mWebpageUrl, mDescription, mPhoto,
                        mHashTag, Mode.Automatic,mShareCallback )
            }

            R.id.tw_inner_btn -> {
                RTwitterManager.instance.share(this, mWebpageUrl, mDescription, mPhoto,
                        mHashTag, Mode.Native, mShareCallback )
            }
            R.id.ins_app_btn -> {
                RInstagramManager.instance.shareImage(this, mPhoto, Mode.Native)
            }

            R.id.ins_sys_btn -> {
                RInstagramManager.instance.shareImage(this, mPhoto, Mode.System)
            }
            R.id.ins_vid_btn -> {
                flag = Instagram
                chooseVideo()
            }

            R.id.wx_text_btn -> {
                RWechatManager.instance.shareText(this, mDescription, RWechatManager.TargetScene
                        .Session, mShareCallback)

            }

            R.id.wx_img_btn -> {
                RWechatManager.instance.shareImage(this, mPhoto, RWechatManager.TargetScene
                        .Session, mShareCallback)
            }

            R.id.wx_web_btn -> {
                RWechatManager.instance.shareWebpage(this, mWebpageUrl, mTitle, mDescription,
                        mPhoto, RWechatManager.TargetScene.Session, mShareCallback)
            }
            R.id.wx_video_btn -> {
                RWechatManager.instance.shareVideo(this, mVideoUrl,mTitle, mDescription, mPhoto,
                        RWechatManager.TargetScene.Session, mShareCallback)
            }
            R.id.wx_music_btn -> {
                RWechatManager.instance.shareMusic(this, mAudioStreamUrl, mTitle, mDescription,
                        mPhoto, mMusicUrl, RWechatManager.TargetScene.Session, mShareCallback)
            }
            R.id.wx_mini_btn -> {
                RWechatManager.instance.shareMiniProgram(this, "gh_d43f693ca31f",
                        "pages/play/index?cid=fvue88y1fsnk4w2&ptag=vicyao&seek=3219",
                        MINIPROGRAM_TYPE_RELEASE, mWebpageUrl, mTitle, mDescription, mPhoto,
                        RWechatManager.TargetScene.Session, mShareCallback)
            }
            R.id.wx_file_btn -> {
                flag = Wechat
                chooseFile()
            }

            R.id.wb_text_btn -> {
                RSinaWeiboManager.instance.shareText(this, mDescription, mShareCallback)
            }
            R.id.wb_img_btn -> {
                RSinaWeiboManager.instance.sharePhoto(this, arrayListOf(mPhoto), mDescription,
                        true, mShareCallback)
            }
            R.id.wb_vd_btn -> {
                flag = Sina
                chooseVideo()
            }
            R.id.wb_web_btn -> {
                RSinaWeiboManager.instance.shareWebpage(this, mWebpageUrl, mTitle, mDescription,
                        mPhoto, mShareCallback)
            }
            R.id.qq_default_btn -> {
                RQqManager.instance.shareWebpage(this, mWebpageUrl, mTitle, mDescription,
                        mThumbImageUrl, mShareCallback)
            }

            R.id.qq_img_btn -> {
                RQqManager.instance.shareImage(this, mPhoto, mShareCallback)
            }
            R.id.qq_app_btn -> {
                RQqManager.instance.shareApp(this, mAppUrl, mTitle, mDescription,
                        mThumbImageUrl, mShareCallback)
            }

            R.id.qq_music_btn -> {
                RQqManager.instance.shareMusic(this, mAudioStreamUrl, mMusicUrl, mTitle,
                        mDescription, mThumbImageUrl, mShareCallback)
            }
            R.id.qz_web_btn -> {
                RQqManager.instance.shareWebpageToZone(this, mWebpageUrl, mTitle, mDescription,
                        arrayListOf(mThumbImageUrl, mThumbImageUrl, mThumbImageUrl), mShareCallback)
            }
            R.id.qz_imgs_btn -> {
                RQqManager.instance.publishImagesToZone(this, arrayListOf(mPhoto, mPhoto,
                        mPhoto), mDescription, mShareCallback)
            }
            R.id.qz_video_btn -> {
                flag = QZone
                chooseVideo()
            }
            R.id.wsa_img_btn -> {
                RWhatsAppManager.instance.share(this, mPhoto, mDescription)
            }

            R.id.gp_web_btn -> {
                RGooglePlusManager.instance.share(this, mWebpageUrl, mDescription)
            }

            R.id.tmb_text_btn -> {
                RTumblrManager.instance.shareText(this, mDescription, mTitle,mWebpageUrl, mShareCallback)
            }
            R.id.tmb_img_btn -> {
                RTumblrManager.instance.shareImage(this, mThumbImageUrl,mDescription,
                        mWebpageUrl,mShareCallback )
            }

            R.id.l_text_btn -> {
                RLineManager.instance.share(this, mDescription)
            }
            R.id.l_img_btn -> {
                RLineManager.instance.share(this, mPhoto)
            }
            R.id.pin_img_btn -> {
                RPinterestManager.instance.shareImage(this, mThumbImageUrl)
            }
        }

    }

    private fun chooseVideo() {
        val intent = Intent()
        intent.setType("video/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent,
                PICK_VIDEO_REQUEST_CODE)
    }
    private fun chooseFile() {
        val intent = Intent()
        intent.setType("*/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent,
                PICK_FILE_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) return

        val uri : Uri? = data?.data
        if (requestCode == PICK_VIDEO_REQUEST_CODE) {

            when (flag) {
                Facebook -> {
                    uri?.let {
                        RFacebookManager.instance.shareLocalVideo(this, uri)
                    }
                }
                Instagram -> {
                    uri?.let {
                        RInstagramManager.instance.shareVideo(this, uri)
                    }
                }
                Sina -> {
                    uri?.let {
                        RSinaWeiboManager.instance.shareLocalVideo(this, uri, mDescription, false,
                                mShareCallback)
                    }
                }
                QZone -> {
                    uri?.let {
                        RQqManager.instance.publishVideoToZone(this, uri, mDescription, mShareCallback)
                    }
                }
            }

        }
        if (requestCode == PICK_FILE_REQUEST_CODE) {
            when (flag) {
                Wechat -> {
                    uri?.let {
                        RWechatManager.instance.shareFile(this, uri, mTitle, mPhoto,
                                RWechatManager.TargetScene.Session, mShareCallback)
                    }

                }
            }
        }
    }


}
