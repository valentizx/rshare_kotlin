package me.rex.sdk.tumblr

import android.content.Intent
import com.flurry.android.tumblr.PhotoPost
import com.flurry.android.tumblr.TextPost


class RTumblrHelper (val intent : Intent) {

    private val mIntent = intent

    internal val imageParam : PhotoPost
        get() {
            val param = PhotoPost(mIntent.getStringExtra("imageUrl"))
            param.setCaption(mIntent.getStringExtra("description"))
            param.setWebLink(mIntent.getStringExtra("webpageUrl"))
            return param
        }

    internal val textParam : TextPost
        get() {
            val param = TextPost(mIntent.getStringExtra("body"))
            param.setTitle(mIntent.getStringExtra("title"))
            param.setWebLink(mIntent.getStringExtra("webpageUrl"))
            return param
        }
}