package me.rex.sdk.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

fun saveBitmapToExternalSharePath(context: Context, bitmap: Bitmap) {


    val dir = context.externalCacheDir.absolutePath + "/share_tmp/"
    val dirFile = File(dir)

    if (!dirFile.exists()) {
        dirFile.mkdirs()
    }

    val file = File(dirFile, "${System.currentTimeMillis()}" + ".jpg")

    var out : FileOutputStream? = null

    try {
        out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    } catch (e : FileNotFoundException) {
        e.printStackTrace()
    }

    try {
        out?.flush()
        out?.close()
    } catch (e : IOException) {
        e.printStackTrace()
    }
}

fun getExternalSharePathFileUris(context: Context) : ArrayList<Uri>? {

    val dir = context.externalCacheDir.absolutePath + "/share_tmp/"
    val dirFile = File(dir)

    if (!dirFile.isDirectory || !dirFile.exists()) {
        return null
    }


    var uris : ArrayList<Uri> = ArrayList<Uri>()

    for (f in dirFile.listFiles()) {
        if (f.isFile) {
            uris.add(Uri.fromFile(f))
        }
    }
    return uris
}


fun getExternalSharePathFiles(context: Context) : ArrayList<File>? {
    val dir = context.externalCacheDir.absolutePath + "/share_tmp/"
    val dirFile = File(dir)

    if (!dirFile.isDirectory || !dirFile.exists()) {
        return null
    }

    var files = ArrayList<File>()

    for (f in dirFile.listFiles()) {
        if (f.isFile) {
            files.add(f)
        }
    }
    return files
}

fun getExternalSharePathBitmaps(context: Context) : ArrayList<Bitmap>? {
    val dir = context.externalCacheDir.absolutePath + "/share_tmp/"
    val dirFile = File(dir)

    if (!dirFile.isDirectory || !dirFile.exists()) {
        return null
    }
    var bitmaps = ArrayList<Bitmap>()

    for (f in dirFile.listFiles()) {
        if (f.isFile) {
            bitmaps.add(BitmapFactory.decodeFile(f.absolutePath))
        }
    }
    return bitmaps
}

fun getExternalSharePathFilePaths(context: Context) : ArrayList<String>? {

    var paths = ArrayList<String>()
    var uris = getExternalSharePathFileUris(context)

    uris?.let {
        for (i in 0 until uris.size) {
            getPath(context, uris.get(i))?.let{
                paths.add(getPath(context, uris.get(i))!!)
            }

        }
        return paths
    }
    return  null
}


fun deleteExternalShareDir(context: Context) : Boolean {

    val dir = context.externalCacheDir.absolutePath + "/share_tmp/"
    val dirFile = File(dir)

    if (!dirFile.isDirectory || !dirFile.exists()) {
        return false
    }
    var flag = true
    for (f in dirFile.listFiles()) {
        if (f.isFile) {
            flag = deleteFile(f.absolutePath)
            if (!flag) break
        } else if (f.isDirectory) {
            f.delete()
            if (!flag) break
        }
    }
    if (!flag) return false
    return dirFile.delete()
}

private fun deleteFile(fileName : String) : Boolean {
    val file = File(fileName)
    if (file.exists() && file.isFile) {
        return file.delete()
    } else {
        return false
    }
}


fun getPath(context: Context, uri: Uri) : String? {


    val isKitKat = Build.VERSION.SDK_INT >= 19

    if (isKitKat && isDocumentUri(context, uri)) {
        if (isExternalStorageDocument(uri)) {
            val docId = getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            if ("primary".equals(type, true)) {
                return "${Environment.getExternalStorageDirectory()}" + "/" + split[1]
            }

        } else if (isDownloadsDocument(uri)) {
            val docId = getDocumentId(uri)

            if (!docId.isNullOrEmpty()) {
                if (docId.startsWith("raw:")) {
                    return docId.replaceFirst("raw:".toRegex(), "")
                }
            }
            try {
                val contentUri = ContentUris.withAppendedId(Uri.parse
                ("content://downloads/public_downloads"), docId.toLong())
                return getDataColumn(context, contentUri, null, null )
            } catch (e: NumberFormatException) {
                return null
            }

        } else if (isMediaDocument(uri)) {
            val docId = getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            var contentUri : Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return getDataColumn(context, contentUri, selection, selectionArgs)

        }
    } else if ("content".equals(uri.scheme, true)) {
        if (isGooglePhotosUri(uri)) {
            return uri.lastPathSegment
        }
        return getDataColumn(context, uri ,null , null)
    } else if ("file".equals(uri.scheme, true)) {
        return uri.path
    }
    return null

}


private const val PATH_DOCUMENT = "document"

fun isDocumentUri(context: Context, uri: Uri) : Boolean {

    var paths = uri.pathSegments

    if (paths.size < 2 || !PATH_DOCUMENT.equals(paths.get(0)))  {
        return false
    }
    return true

}

fun getDocumentId(documentUri: Uri) : String {
    val paths : List<String> = documentUri.pathSegments
    if (paths.size < 2) {
        throw IllegalArgumentException(("Not a document: " + documentUri))
    }
    if (!PATH_DOCUMENT.equals(paths.get(0))) {
        throw IllegalArgumentException(("Not a document: " + documentUri))
    }
    return paths.get(1)
}

fun getDataColumn(context: Context, uri: Uri?, selection : String?, selectionArgs : Array<String>?)
        :String? {

    var cursor : Cursor? = null

    val column = "_data"

    val  projection = arrayOf(column)

    try {
        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor.let {
            cursor?.close()
        }
    }

    return null
}

fun isExternalStorageDocument(uri: Uri) : Boolean {
    return "com.android.externalstorage.documents".equals(uri.authority)
}

fun isDownloadsDocument(uri: Uri) : Boolean {
    return "com.android.providers.downloads.documents".equals(uri.authority)
}

fun isMediaDocument(uri: Uri) : Boolean {
    return "com.android.providers.media.documents".equals(uri.authority)
}

fun isGooglePhotosUri(uri: Uri) : Boolean {
    return "com.google.android.apps.photos.content".equals(uri.authority)
}


fun detectFileUriExposure() {
    val sb = StrictMode.VmPolicy.Builder()
    StrictMode.setVmPolicy(sb.build())
    sb.detectFileUriExposure()
}