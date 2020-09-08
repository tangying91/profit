package org.profit.util

import java.io.File

object PathUtils {

    var rootPath: String? = null

    private val CONF_PATH = File.separator + "conf" + File.separator
    private val WEBROOT_PATH = File.separator + "webroot" + File.separator
    private val LOCALE_PATH = File.separator + "locale" + File.separator

    val confPath: String
        get() = rootPath + CONF_PATH

    val webRootPath: String
        get() = rootPath + WEBROOT_PATH

    val localePath: String
        get() = rootPath + LOCALE_PATH

    init {
        rootPath = File("").absolutePath
    }
}