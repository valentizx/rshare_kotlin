package me.rex.sdk.twitter

import android.content.Context
import android.content.Intent
import com.twitter.sdk.android.core.TwitterCore

internal typealias RTwitterAuthCallback = ((state : Int) -> Unit)
class RTwitterAuthHelper private constructor(){

    companion object {
        val instance : RTwitterAuthHelper get() = Inner.instance

    }
    private object Inner {
        val instance = RTwitterAuthHelper()
    }

    @Volatile internal var hasLogged : Boolean = false
        get() {

            return TwitterCore.getInstance().sessionManager.activeSession !=  null
        }
        private set

    internal lateinit var authCallback : RTwitterAuthCallback
        private set

    internal fun authorizeTwitter(context: Context, callback: RTwitterAuthCallback) {
        authCallback = callback
        val intent = Intent(context, RTwitterAuthActivity::class.java)
        context.startActivity(intent)

    }

}