package me.rex.sdk.googleplus

import android.content.Context
import android.net.Uri
import com.google.android.gms.plus.PlusShare
import me.rex.sdk.RShare

class RGooglePlusManager private constructor(): RShare() {
    companion object {
        val instance : RGooglePlusManager get() = Inner.instance
    }
    private object Inner {
        val instance = RGooglePlusManager()
    }
    fun share(context: Context, webpageUrl : String?, text :String) {
        val intent = PlusShare.Builder(context)
                .setType("text/plain")
                .setText(text)
                .setContentUrl(Uri.parse(webpageUrl ?: ""))
                .intent

        context.startActivity(intent)
    }
}