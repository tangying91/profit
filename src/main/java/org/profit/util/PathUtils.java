package org.profit.util;

import java.io.File;

/**
 * Path utilities
 *
 * @author Kyia
 */
public class PathUtils {

	private static String rootPath;

	private static final String CONF_PATH = File.separator + "conf" + File.separator;
	private static final String WEBROOT_PATH = File.separator + "webroot" + File.separator;
    private static final String LOCALE_PATH = File.separator + "locale" + File.separator;

	static {
		rootPath = new File("").getAbsolutePath();
	}

	public static String getRootPath() {
		return rootPath;
	}

	public static String getConfPath() {
		return getRootPath() + CONF_PATH;
	}

	public static String getWebRootPath() {
		return getRootPath() + WEBROOT_PATH;
	}

    public static String getLocalePath() {
        return getRootPath() + LOCALE_PATH;
    }
}
