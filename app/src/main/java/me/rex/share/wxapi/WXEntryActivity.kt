package me.rex.share.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import me.rex.sdk.wechat.RWechatManager

class WXEntryActivity : Activity(), IWXAPIEventHandler {

    private val mManager = RWechatManager.instance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!mManager.handleIntent(this, intent)) {
            finish()
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            if (!mManager.handleIntent(this, intent)) finish()
        }

    }

    override fun onReq(p0: BaseReq?) {
        p0?.let {
            mManager.onReq(this, p0)
        }
    }

    override fun onResp(p0: BaseResp?) {

        p0?.let {
            mManager.onResp(this, p0)
        }
    }
}