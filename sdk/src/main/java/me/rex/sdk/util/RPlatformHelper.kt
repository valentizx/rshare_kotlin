package me.rex.sdk.util

import android.content.Context
import me.rex.sdk.share.RSharePlatform
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException


fun getWechatAppId(context : Context) : String? {

    val inputStream = getInputStream(context)

    var wxe : Element? = getRootElement("WeChat", inputStream!!)

    wxe?.let {
        return wxe.getAttribute("AppId")
    }
    return null
}

fun getTwitterConsumerKey(context: Context) : String? {

    val inputStream = getInputStream(context)

    var te : Element? = getRootElement("Twitter", inputStream!!)

    te?.let {
        return te.getAttribute("ConsumerKey")
    }
    return null
}

fun getTwitterConsumerSecret(context: Context) : String? {
    val inputStream = getInputStream(context)

    var te : Element? = getRootElement("Twitter", inputStream!!)

    te?.let {
        return te.getAttribute("ConsumerSecret")
    }
    return null
}

fun getSinaAppKey(context: Context) : String?{

    val inputStream = getInputStream(context)

    var se : Element? = getRootElement("Sina", inputStream!!)

    se?.let {
        return se.getAttribute("AppKey")
    }
    return null
}

fun getSinaAppSecret(context: Context) : String? {
    val inputStream = getInputStream(context)

    var se : Element? = getRootElement("Sina", inputStream!!)

    se?.let {
        return se.getAttribute("AppSecret")
    }
    return null
}

fun getSinaAppRedirectUrl(context: Context) : String? {
    val inputStream = getInputStream(context)

    var se : Element? = getRootElement("Sina", inputStream!!)

    se?.let {
        return se.getAttribute("RedirectUrl")
    }
    return null
}

fun getSinaScope(context: Context) : String? {
    val inputStream = getInputStream(context)

    var se : Element? = getRootElement("Sina", inputStream!!)

    se?.let {
        return se.getAttribute("Scope")
    }
    return null
}

fun getQQAppId(context: Context) : String? {
    val inputStream = getInputStream(context)

    val qqe = getRootElement("QQ", inputStream!!)

    return qqe!!.getAttribute("AppID")

}


fun getTumblrConsumerKey(context: Context) : String? {
    val inputStream = getInputStream(context)

    val te = getRootElement("Tumblr", inputStream!!)

    return te!!.getAttribute("ConsumerKey")
}

fun getTumblrConsumerSecret(context: Context) : String? {
    val inputStream = getInputStream(context)

    val te = getRootElement("Tumblr", inputStream!!)

    return te!!.getAttribute("ConsumerSecret")
}

fun getTumblrFlurryKey(context: Context) : String? {
    val inputStream = getInputStream(context)

    val te = getRootElement("Tumblr", inputStream!!)

    return te!!.getAttribute("FlurryKey")
}


private fun getInputStream(context : Context) : InputStream? {
    var inputStream : InputStream? = null
    try {
        inputStream = context.assets.open("RShare.xml")
    } catch (e : IOException) {
        e.printStackTrace()
    }
    return inputStream
}


private fun getRootElement(rootElementName : String, inputStream: InputStream) : Element? {

    val factory : DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    var element : Element? = null
    try {

        val builder : DocumentBuilder = factory.newDocumentBuilder()
        val document : Document = builder.parse(inputStream)
        val rootElement : Element = document.documentElement
        element = rootElement.getElementsByTagName(rootElementName).item(0) as Element?

    } catch (e : ParserConfigurationException) {
        e.printStackTrace()

    } catch (e : SAXException) {
        e.printStackTrace()
    } catch (e : IOException) {
        e.printStackTrace()
    }
    return element
}


fun isInstalled(context: Context, platform: RSharePlatform) : Boolean {
    var packageName : String = ""
    when (platform) {
        RSharePlatform.Facebook -> packageName = "com.facebook.katana"
        RSharePlatform.Twitter -> packageName = "com.twitter.android"
        RSharePlatform.WeChat -> packageName = "com.tencent.mm"
        RSharePlatform.Sina -> packageName = "com.sina.weibo"
        RSharePlatform.Instagram -> packageName = "com.instagram.android"
        RSharePlatform.WhatsApp -> packageName = "com.whatsapp"
        RSharePlatform.QQ -> packageName = "com.tencent.mobileqq"
        RSharePlatform.Line -> packageName = "jp.naver.line.android"
        RSharePlatform.Pinterest -> packageName = "com.pinterest"

    }
    val pm = context.packageManager
    val pinfo = pm.getInstalledPackages(0)
    pinfo?.let {
        for (i in 0 until pinfo.size) {
            val pn = pinfo.get(i).packageName
            if (pn.equals(packageName)) return true
        }
    }
    return false
}