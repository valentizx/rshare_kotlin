package me.rex.sdk

enum class ShareContentType {
    Webpage,
    Video,
    Photo,
    Music,
    Media,
    Text,
    App,
    File
}

enum class ShareState {
    Success,
    Failure,
    Cancel
}

enum class Mode {
    /**
     * 默认的分享方式
     * Facebook: 优先客户端分享, 客户端无法分享会转由网页形式分享.
     * Twitter: 优先应用内分享.
     */
    Automatic,
    /**
     * 原生应用分享
     * Facebook、Twitter: 无回调.
     *
     */
    Native,
    /**
     * 网页分享, 有回调, 仅对Facebook生效.
     */
    Web,
    /**
     * 反馈网页形式分享, 有回调, 仅对Facebook生效.
     */
    Feed,
    /**
     * 调用 Android 系统分享.
     */
    System,

}
typealias RShareCallback = ((platform : RSharePlatform, state : ShareState, errorInfo : String?) ->
Unit)
open class RShare {}