package com.sample.util

object Constants {

    // Database
    private const val mongoPw = "admin"
    const val DATABASE_NAME = "sample_backend"
    const val DATABASE_URL = "mongodb+srv://admin:$mongoPw@cluster0.i7nzu4x.mongodb.net/?retryWrites=true&w=majority"

    // Page Size
    const val DEFAULT_POST_PAGE_SIZE = 15
    const val DEFAULT_Activity_PAGE_SIZE = 15
    const val DEFAULT_MESSAGE_PAGE_SIZE = 15
    const val MAX_COMMENT_LENGTH = 2000

    // Url and File Path
    const val BASE_URL = "http://47.115.229.205:8081/"
//    const val BASE_URL = "http://172.28.188.188:8081/"
    const val PROFILE_PICTURE_PATH = "build/resources/main/static/profile_pictures/"
    const val BANNER_IMAGE_PATH = "build/resources/main/static/banner_images/"
    const val POST_CONTENT_PATH = "build/resources/main/static/post_contents/"
//    const val PROFILE_PICTURE_PATH = "root/app/profile_pictures/"
//    const val BANNER_IMAGE_PATH = "root/app/banner_images/"
//    const val POST_CONTENT_PATH = "root/app/post_contents/"
    const val DEFAULT_PROFILE_PICTURE_PATH = "${BASE_URL}profile_pictures/bot.png"
    const val DEFAULT_BANNER_IMAGE_PATH = "${BASE_URL}profile_pictures/bot.png"

    // One Signal
    const val ONESIGNAL_APP_ID = "ec3c3755-1b13-4565-a0ab-40190f974b80"
    const val ONESIGNAL_NOTIFICATION_URL = "https://onesignal.com/api/v1/notifications"
    const val ONESIGNAL_API_KEY = "ZDI0MTJkMzctOWQ5Ny00MDg4LTg2NjQtNDFkZjZjMjczMGMy"

}
