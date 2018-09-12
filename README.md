# RSHARE Kotlin Version

RSHARE 这个 Demo 中支持: 微信、QQ、新浪微博、Facebook、GooglePlus(Google +)、Twitter、WhatsApp、Line、Tumblr、Instagram、Pinterest 11 个 Social 平台.

❤️🧡💛💚💙💜🖤
[详细设计、注意事项](https://rexzx.github.io/2018/08/29/rshare-android-version/)
❤️🧡💛💚💙💜🖤

### QQ
#### 准备
分享需要注册平台, [腾讯开发者主页](http://open.qq.com/), [SDK 下载](http://wiki.open.qq.com/wiki/mobile/SDK%E4%B8%8B%E8%BD%BD), QQ SDK 目前**不支持 compile 集成**, Android API 调用说明[文档](http://wiki.open.qq.com/wiki/mobile/API%E8%B0%83%E7%94%A8%E8%AF%B4%E6%98%8E).

#### 集成
a. 手动添加 SDK 到 ``libs`` 文件夹, 并:

![Snip20180830_7](https://lh3.googleusercontent.com/-7DCJppBupJA/W4eaDI_LncI/AAAAAAAAAUs/hpVlStBvt_celCw2za386XN9rVRPD932ACHMYCw/I/Snip20180830_7.png)

b. 在 **AndroidManifest.xml** 的 ``<application>`` 节点下增加:

```xml
<activity
       android:name="com.tencent.tauth.AuthActivity"
       android:noHistory="true"
       android:launchMode="singleTask" >
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
       	<category android:name="android.intent.category.DEFAULT" />
       	<category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="tencentYOURAPPID" />
    </intent-filter>
</activity>
```
c. 添加以下权限:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
```
#### 接口及内部实现

**a. 内部初始化 SDK**

```kotlin
private lateinit var mTencent : Tencent
mTencent = Tencent.createInstance(appId, context)
```


**b. 分享**


```
内部:
private var mMainHandlerLock = Any()
internal val mainHandler : Handler = Handler()
    get() {
        if (field == null) {
            synchronized(mMainHandlerLock) {
                if (field == null) {
                    field = Handler(Looper.getMainLooper())
                }
            }
        }
        return field
    }
调用:
mainHandler.post(
    Runnable {
        kotlin.run {
            /// ...
       }
 )
```

**网页分享:**

```kotlin
RQqManager.instance.shareWebpage(context, webpageUrl, title, description, thumbImageUrl, callback)
```


**图片分享:**

```kotlin
RQqManager.instance.shareImage(context, targetImage, callback)
```


**音频链分享:**

```kotlin
RQqManager.instance.shareMusic(context, audioStreamUrl, musicWebpageUrl, title, description, thumbImageUrl, callback)
```


**应用分享:**


```kotlin
RQqManager.instance.shareApp(context, appUrl, title, description, thumbImageUrl, callback)
```

**分享网页到空间:**


```kotlin
RQqManager.instance.shareWebpageToZone(context, webpageUrl, title, description, imageUrlList, callback)
```

**分享图片到空间:**


```kotlin
RQqManager.instance.publishImagesToZone(context, targetImages, description, callback)
```

**分享本地视频到空间:**

```kotlin
RQqManager.instance.publishVideoToZone(context, localVideoUrl, description, callback)
```


### 微信
#### 准备

分享需要注册平台, [微信开放平台](https://open.weixin.qq.com/), [SDK 下载](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419319167&token=&lang=zh_CN), 微信 SDK **支持 compile 集成**, [分享 & 收藏 API 调用说明](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340&token=&lang=zh_CN).

#### 集成
a. 
在 **Application 级 ``build.gradle`` 中配置:**

```xml
dependencies {
    compile 'com.tencent.mm.opensdk:wechat-sdk-android-with-mta:+'
}
```
或者

``` xml
dependencies {
    compile 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:+'
}
```
前者包括统计功能.

b. 添加以下权限:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

c. 当需要在分享完毕后接受微信的传值需要**在你的包名相应目录下新建一个 wxapi 目录，并在该 wxapi 目录下新增一个 ``WXEntryActivity`` 类，该类继承自 ``Activity``**, 在 **AndroidManifest.xml** 的 ``<application>`` 节点下增加:

```xml
<activity
    android:name=".wxapi.WXEntryActivity"
    android:exported="true"
/>
```

#### 接口及内部实现

**a. 内部初始化 SDK**

```kotlin
val mIWXApi = WXAPIFactory.createWXAPI(context, appId)
mIWXApi.registerApp(appId)
```

**b. 分享**

**文字分享:**


```kotlin
RWechatManager.instance.shareText(context, description, scene, callback)
```

**图片分享:**


```kotlin
RWechatManager.instance.shareImage(context, targetImage, scene, callback)
```

**网页分享:**


```kotlin
RWechatManager.instance.shareWebpage(context, webpageUrl, title, description, thumbImage, scene, callback)
```


**视频链分享:**
实质就是网页的分享, 在此不作代码示例.

**音频链分享:**


```kotlin
RWechatManager.instance.shareMusic(context, audioStreamUrl, title, description, thumbImage, musicWebapgeUrl, scene, callback)
```

**小程序分享:**

```kotlin
RWechatManager.instance.shareMiniProgram(context, userName, path, MINIPROGRAM_TYPE, webpageUrl,title, description, thumbImage, scene, callback)
```

**文件分享:**

```kotlin
RWechatManager.instance.shareFile(context, localFileUrl, title, thumbImage, scene, callback)
```

### 新浪
#### 准备

分享需要注册平台, [新浪开放平台](http://open.weibo.com/), [SDK 下载](http://open.weibo.com/wiki/SDK#android_SDK), 新浪 SDK **支持 compile 集成**, [Android 接口调用文档](https://github.com/sinaweibosdk/weibo_android_sdk).

#### 集成
a. 
在 **Project 级 ``build.gradle`` 中配置:**

```xml
allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://dl.bintray.com/thelasterstar/maven/" }
    }
}
```

在 **Application 级 ``build.gradle`` 中配置:**

```xml
dependencies {
    compile 'com.sina.weibo.sdk:core:4.3.0:openDefaultRelease@aar'
}
```


#### 接口及内部实现

**a. 内部初始化 SDK**

```kotlin
WbSdk.install(context, AuthInfo(context, key, redirectUrl, scope))
val handler = WbShareHandler(context as Activity)
handler.registerApp()
```

**b. 分享**

**文字分享:**

```kotlin
 RSinaWeiboManager.instance.shareText(context, text, callback)
```

**图片分享:**

```kotlin
RSinaWeiboManager.instance.sharePhoto(context, targetImages, description, isToStory(true or false), callback)
```


**本地视频分享:**


```kotlin
RSinaWeiboManager.instance.shareLocalVideo(context, localVideoUrl, description, isToStory(true or false), callback)
```


**网页分享:**

```kotlin
RSinaWeiboManager.instance.shareWebpage(context, webpageUrl, title, description, thumbImage,callback)
```

### Facebook
#### 准备
分享需要注册平台, [Facebook 开发者主页](https://developers.facebook.com/), Facebook SDK **支持 compile 集成**, [分享接口调用说明](https://developers.facebook.com/docs/sharing/android).
#### 集成

a. 
在 **Project 级 ``build.gradle`` 中配置:**

```xml
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}
```

在 **Application 级 ``build.gradle`` 中配置:**

```xml
dependencies {
    implementation 'com.facebook.android:facebook-share:4.34.0'
}
```
b. 在 **AndroidManifest.xml** 的 ``<application>`` 节点下增加:

```xml
<meta-data android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/facebook_app_id"/>

<provider android:authorities="com.facebook.app.FacebookContentProviderYOURAPPID"
          android:name="com.facebook.FacebookContentProvider"
          android:exported="true" />
<activity android:name="com.facebook.CustomTabActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />

        <data android:scheme="fbYOURAPPID" />
    </intent-filter>
</activity>
```



#### 接口调用及内部实现

**a. 内部初始化 SDK**

Facebook 分享在配置 **AndroidManifest.xml** 后无需代码代码初始化.

**b. 分享**


**网页分享:**


```kotlin
RFacebookManager.instance.shareWebpage(context, webpageUrl, description, hashTag, mode, callback)
```


**图片分享:**

```kotlin
RFacebookManager.instance.sharePhoto(context, targetImages)
```


**本地视频分享:**


```kotlin
RFacebookManager.instance.shareLocalVideo(context, localVideoUrl)
```
**c. 获取散列**

``RFacebookManager`` 提供了获取散列的方法.

### Twitter
#### 准备
分享需要注册平台, [Twitter 开发者主页](https://developer.twitter.com/content/developer-twitter/en.html), [注册应用主页](https://apps.twitter.com/), Twitter SDK **支持 compile 集成**, [分享接口调用说明](https://github.com/twitter/twitter-kit-android/wiki/Compose-Tweets).

**⚠️: Twitter SDK 将于 2018/10/31 后不再进行维护, 但是不影响后续使用, 需自行维护, [Twitter 产品经理 Neil Shah 对 Twitter SDK 放弃维护迭代的声明博客](https://blog.twitter.com/developer/en_us/topics/tools/2018/discontinuing-support-for-twitter-kit-sdk.html).**

#### 集成

a. 在 **Project 级 ``build.gradle`` 中配置:**

```xml
allprojects {
    repositories {
        google()
        jcenter()
    }
}
```

在 **Application 级 ``build.gradle`` 中配置:**

```xml
dependencies {
    implementation 'com.twitter.sdk.android:tweet-composer:3.1.1'
    implementation 'com.twitter.sdk.android:twitter-core:3.1.1'
}
```

#### 接口调用及内部实现

**a. 内部初始化 SDK**

```kotlin
val config = TwitterConfig.Builder(context)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig(key, secret))
                .debug(true)
                .build()
Twitter.initialize(config)
```


**b. 授权 Twitter 客户端**
登录(授权监听接口以及 callback):

```kotlin
internal typealias RTwitterAuthCallback = ((state : Int) -> Unit)
```

**判断是否登录过:**


```kotlin
RTwitterAuthHelper.instance.hasLogged
```


**登录授权:**

```kotlin
RTwitterAuthHelper.instance.authorizeTwitter(context) { state ->
    when (state) {
        1 -> {
            // ...
        }
        0 -> {
            // ...     
        }
    }
}
```

**c. 分享**

```kotlin
RTwitterManager.instance.share(context, webpageUrl, description, targetImage, hashTag, mode, callback)
```


### Instagram
#### 准备

分享无需注册平台无需 SDK, [Instagram 开发者主页](https://www.instagram.com/developer/), [构建 Intent 方式分享](https://www.instagram.com/developer/mobile-sharing/android-intents/).


#### 接口调用及内部实现

**分享**

**图片分享:**


```kotlin
RInstagramManager.instance.shareImage(context, targetImage)
```

**本地视频分享:**


```kotlin
RInstagramManager.instance.shareVideo(context, localVideoUrl)
```

### Tumblr
#### 准备
分享需要注册平台, [Tumblr 开发者主页](https://www.tumblr.com/developers), [注册应用主页](https://dev.flurry.com/admin/applications), Tumblr SDK **支持 compile 集成**, [分享接口调用说明](https://developer.yahoo.com/flurry/docs/integrateflurry/android/).

#### 集成
a. 此 Demo 中是手动集成的 6.1.0 版本的 SDK.

⚠️: 一定是这个版本的, 最新版本的 SDK 我没有找到分享的接口.


#### 接口调用及内部实现

**a. 内部初始化 SDK**

```java
FlurryAgent.setLogEnabled(true);
FlurryAgent.init(context, flurryKey);
TumblrShare.setOAuthConfig(key, secret);
```


**b. 分享**


**文字分享:**

```kotlin
RTumblrManager.instance.shareText(context, description, title, webpageUrl, callback)
```

**图片链接分享:**

```kotlin
RTumblrManager.instance.shareImage(context, targetImageUrl, description, webpageUrl, callback )
```

### Pinterest
#### 集成
Android 端无需集成 SDK, 仅通过 Intent 方式就可以分享, 但是 iOS 需要.


**b. 分享**


**图片链接分享:**

```kotlin
RPinterestManager.instance.shareImage(context, targetImageUrl)
```


### Line
#### 集成
无需集成 SDK, 仅通过 Intent 方式就可以分享.


**b. 分享**


**文字分享:**

```kotlin
RLineManager.instance.share(context, text)
```

**图片分享:**

```kotlin
RLineManager.instance.share(context, targetImage)
```


### WhatsApp
#### 准备
分享无需注册平台.

Android 端无需集成 SDK, 仅通过 Intent 方式就可以分享, 但是 iOS 需要.


**b. 分享**


**图文分享:**

```kotlin
RWhatsAppManager.instance.share(context, targetImage, description)
```
### GooglePlus
#### 准备
需要注册平台, [Google Plus 开发者主页](https://developers.google.com/+/)
, [创建流程](https://developers.google.com/+/mobile/android/getting-started).

#### 集成
需要 **Google Service 的支持**, 在 **Application 级 ``build.gradle`` 中配置:**

```xml
dependencies {
    implementation 'com.google.android.gms:play-services-plus:15.0.1'
}
```

#### 接口调用及内部实现

**分享网页:**

```kotlin
RGooglePlusManager.instance.share(context, webpageUrl, description)
```

## 统一分享接口



### 类图

![Android 统一接口类图](https://lh3.googleusercontent.com/-O0rTTel0iHc/W5eJdrZJVII/AAAAAAAAAbI/JdickRAMap45EJT6Lrmizg1X6z9mJqtOACHMYCw/I/Android%2B)

- **RShareManger:** 主分享 Manager, 子平台 Manager 的初始化、分享、应用跳转和一些其他操作都在此进行;
- **RImageContent、RVideoContent、RTextContent、RWebpageContent** 为四种对应分享内容模型.


### 接口

**构建分享模型**

以 ``RImageContent`` 为例:

Java:

```java
RImageContent content = new RImageContent.Builder(targetImage, targetImageUrl)
                 // ...
                .build();
```

Kotlin:

```kotlin
val content : RImageContent = RImageContent().apply { 
            // ...
}
```

**分享：**

以分享 ``RImageContent`` 为例:

Java:

```java
RShareManager.getInstance().shareImage(context, content, channel, new RShareListener() {
            @Override
            public void onComplete(RSharePlatform.Platform platform) {
            // ...  
            }

            @Override
            public void onFail(RSharePlatform.Platform platform, String errorInfo) {
            // ...
            }

            @Override
            public void onCancel(RSharePlatform.Platform platform) {
            // ...
            }
        });
```

Kotlin:

```kotlin
 RShareManager.instance.shareImage(context, content, channel) { platform, state, errorInfo ->
           // ...
}
```




