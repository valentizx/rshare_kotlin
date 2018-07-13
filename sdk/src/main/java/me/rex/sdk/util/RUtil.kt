package me.rex.sdk.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import java.io.ByteArrayOutputStream

fun bmpToByteArray(bmp : Bitmap, needRecycle : Boolean) : ByteArray {

    var i : Int
    var j : Int

    if (bmp.height > bmp.width) {
        i = bmp.width
        j = bmp.width
    } else {
        i = bmp.height
        j = bmp.height
    }


    var localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565)

    var localCanvas = Canvas(localBitmap)

    while (true) {
        localCanvas.drawBitmap(bmp, Rect(0,0,i,j), Rect(0,0,i,j), null)
        if (needRecycle) {
            bmp.recycle()
        }
        val localByteArrayOutputStream = ByteArrayOutputStream()

        localBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream)
        localBitmap.recycle()
        val arrayOfByte : ByteArray = localByteArrayOutputStream.toByteArray()
        try {
            localByteArrayOutputStream.close()
            return arrayOfByte
        } catch (e : Exception) {
            e.printStackTrace()
        }
        i = bmp.height
        j = bmp.height

    }



}