package me.rex.sdk.util

import android.os.Handler
import android.os.Looper


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

