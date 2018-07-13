package me.rex.sdk.twitter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.twitter.sdk.android.core.*

import com.twitter.sdk.android.core.identity.TwitterAuthClient

class RTwitterAuthActivity : Activity() {

    private val authCallback = RTwitterAuthHelper.instance.authCallback
    private val mClient = TwitterAuthClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mClient.authorize(this, object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                authCallback(1)
                finish()
            }

            override fun failure(exception: TwitterException) {
                authCallback(0)
                finish()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mClient.onActivityResult(requestCode, resultCode, data)
    }
}