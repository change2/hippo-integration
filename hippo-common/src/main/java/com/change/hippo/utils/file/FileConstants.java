package com.change.hippo.utils.file;


import org.apache.commons.lang3.StringUtils;
import com.change.hippo.utils.env.EnvHelpers;

public class FileConstants {

    private static final String uplodaPath = "/usr/local/data/cephdata/";


    private static String MEDIA_IMAG_URL = EnvHelpers.me().get("MEDIA_IMG_URL");


    /**
     * 返回upload上传的路径(不推荐)
     *
     * @param fileName 相对路径
     * @return
     */
    public static String getUplodaPath(String fileName) {
        return getuploadPath("", fileName);
    }

    /**
     * 返回upload上传的路径
     *
     * @param relativePath 相对路径
     * @param fileName     文件名(带后缀)
     * @return
     */
    public static String getuploadPath(String relativePath, String fileName) {
        if (!isValidFileName(fileName)) {
            return null;
        }
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1, fileName.length());
        }
        return uplodaPath + formatRelativePath(relativePath) + fileName;
    }

    /**
     * 获取文件访问的url路径(不推荐)
     *
     * @param fileName 文件名(带后缀)
     * @return
     */
    public static String getFileUrl(String fileName) {
        return getFileUrl("", fileName);
    }

    /**
     * 获取文件访问的url路径
     *
     * @param relativePath 相对路径
     * @param fileName     文件名(带后缀)
     * @return
     */
    public static String getFileUrl(String relativePath, String fileName) {
        if (!isValidFileName(fileName)) {
            return null;
        }
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1, fileName.length());
        }
        if (StringUtils.isBlank(MEDIA_IMAG_URL)){
            MEDIA_IMAG_URL = "http://sxwg.sh.189.cn/media/";
        }
        return MEDIA_IMAG_URL + formatRelativePath(relativePath) + fileName;
    }

    public static boolean isValidFileName(String fileName) {
        if (StringUtils.isBlank(fileName) || fileName.length() > 255)
            return false;
        return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
    }

    /**
     * @param relativePath
     * @return
     */
    private static String formatRelativePath(String relativePath) {
        if (StringUtils.isBlank(relativePath))
            return "";
        if (relativePath.startsWith("/"))
            relativePath = relativePath.substring(1, relativePath.length());
        if (!relativePath.endsWith("/"))
            relativePath = relativePath + "/";
        return relativePath;
    }

    public static void main(String[] args) {
        System.out.println(FileConstants.getuploadPath("", "aa.txt"));
        System.out.println(FileConstants.getuploadPath("/user/", "aa.txt"));
        System.out.println(FileConstants.getuploadPath("user/", "aa.txt"));

        System.out.println(FileConstants.getFileUrl("bb.txt"));
        System.out.println(FileConstants.getFileUrl("", "bb.txt"));
        System.out.println(FileConstants.getFileUrl("/user/", "bb.txt"));
        System.out.println(FileConstants.getFileUrl("user/", "bb.txt"));
    }
}
