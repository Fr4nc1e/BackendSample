package com.sample.util

object Constants {

    private const val mongoPw = "admin"
    const val DATABASE_NAME = "sample_backend"
    const val DATABASE_URL = "mongodb+srv://admin:$mongoPw@cluster0.i7nzu4x.mongodb.net/?retryWrites=true&w=majority"

    const val DEFAULT_POST_PAGE_SIZE = 15
    const val DEFAULT_Activity_PAGE_SIZE = 15
    const val DEFAULT_MESSAGE_PAGE_SIZE = 15
    const val MAX_COMMENT_LENGTH = 2000

    const val BASE_URL = "http://47.115.229.205:8081/"
//    const val BASE_URL = "http://172.28.211.51:8081/"
    const val PROFILE_PICTURE_PATH = "build/resources/main/static/profile_pictures/"
    const val BANNER_IMAGE_PATH = "build/resources/main/static/banner_images/"
    const val POST_CONTENT_PATH = "build/resources/main/static/post_contents/"
    const val DEFAULT_PROFILE_PICTURE_PATH = "${BASE_URL}profile_pictures/bot.png"
    const val DEFAULT_BANNER_IMAGE_PATH = "${BASE_URL}profile_pictures/bot.png"
}
