package com.change.hippo.utils.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by mike on 16/6/15.
 */
public class FileHandleUtil {

    /**
     * 写文件到本地
     *
     * @param fullFileName 文件存放真实路径
     * @param inputStream  文件的输入流
     * @return
     * @throws IOException
     */
    public static File writeFile(String fullFileName, InputStream inputStream, int maxSize) throws IOException {

        File file = createFile(fullFileName);

        if (file != null) {
            FileOutputStream outputStream;
            outputStream = new FileOutputStream(fullFileName);

            int readBytes;
            byte[] buffer = new byte[maxSize];
            while ((readBytes = inputStream.read(buffer, 0, maxSize)) != -1) {
                outputStream.write(buffer, 0, readBytes);
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return file;
    }

    /**
     * 写文件到本地 (可追加)
     *
     * @param fullFileName 文件存放真实路径
     * @param inputStream  文件的输入流
     * @return
     * @throws IOException
     */
    public static File writeFile(String fullFileName, InputStream inputStream, int maxSize, boolean append) throws IOException {

        File file = createFile(fullFileName);

        if (file != null) {
            FileOutputStream outputStream;
            outputStream = new FileOutputStream(fullFileName, append);

            int readBytes;
            byte[] buffer = new byte[maxSize];
            while ((readBytes = inputStream.read(buffer, 0, maxSize)) != -1) {
                outputStream.write(buffer, 0, readBytes);
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return file;
    }

    /**
     * 写文件到本地
     *
     * @param fullFileName 文件存放真实路径
     * @param data         文件的字节
     * @return
     * @throws IOException
     */
    public static File writeFile(String fullFileName, byte[] data) throws IOException {

        if (StringUtils.isBlank(fullFileName) || data == null || data.length == 0) {
            return null;
        }
        File file = createFile(fullFileName);

        if (file != null) {
//            InputStream inputStream = null;
//            byte[] buffer = data;
//            int readBytes;
            FileOutputStream outputStream;
            outputStream = new FileOutputStream(fullFileName);
            outputStream.write(data);
//            while ((readBytes = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, readBytes);
//            }
//            if (inputStream != null) {
//                inputStream.close();
//            }
            if (outputStream != null) {
                outputStream.close();
            }

        }
        return file;
    }

    /**
     * 写文件到本地 (可追加)
     *
     * @param fullFileName 文件存放真实路径
     * @param data         文件的字节
     * @return
     * @throws IOException
     */
    public static File writeFile(String fullFileName, byte[] data, boolean append) throws IOException {

        if (StringUtils.isBlank(fullFileName) || data == null || data.length == 0) {
            return null;
        }
        File file = createFile(fullFileName);

        if (file != null) {
//            InputStream inputStream = null;
//            byte[] buffer = data;
//            int readBytes;
            FileOutputStream outputStream;
            outputStream = new FileOutputStream(fullFileName, append);
            outputStream.write(data);
//            while ((readBytes = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, readBytes);
//            }
//            if (inputStream != null) {
//                inputStream.close();
//            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return file;
    }

    /**
     * 使用 apache的写入文件帮助类 实现写文件
     *
     * @param fullFileName 文件存放真实路径
     * @param data         文件的字节数组
     * @return
     * @throws IOException
     */
    public static File writeFileForApache(String fullFileName, byte[] data) throws IOException {

        if (StringUtils.isBlank(fullFileName) || data == null || data.length == 0) {
            return null;
        }
        File file = createFile(fullFileName);

        if (file != null) {
            FileUtils.write(file, new String(data, "utf-8"));
        }
        return file;
    }

    /**
     * 读取文件内容
     *
     * @param fullFileName
     * @return byte 返回字节数组
     * @throws IOException
     */
    public static byte[] readFileForApacheToByte(String fullFileName) throws IOException {
        if (StringUtils.isBlank(fullFileName)) {
            return null;
        }
        File file = new File(fullFileName);
        if (file != null) {
            return FileUtils.readFileToByteArray(file);
        }
        return null;
    }

    /**
     * 读取文件内容
     *
     * @param fullFileName
     * @return 返回字符串
     */
    public static String readFileForApacheToString(String fullFileName) throws IOException {

        if (StringUtils.isBlank(fullFileName)) {
            return null;
        }
        File file = new File(fullFileName);
        if (file != null) {
            return FileUtils.readFileToString(file, (String) null);
        }
        return null;
    }


    /**
     * 读取文件内容
     *
     * @param fullFileName
     * @return 返回字符串
     */
    public static String readFileForApacheToString(String fullFileName, String encoding) throws IOException {

        if (StringUtils.isBlank(fullFileName)) {
            return null;
        }
        File file = new File(fullFileName);
        if (file != null) {
            return FileUtils.readFileToString(file, encoding);
        }
        return null;
    }

    /**
     * 读取文件内容
     *
     * @param fullFileName
     * @return 返回字符串list列表
     */
    public static List<String> readFileForApacheToList(String fullFileName) throws IOException {

        if (StringUtils.isBlank(fullFileName)) {
            return null;
        }
        File file = new File(fullFileName);
        if (file != null) {
            return FileUtils.readLines(file, null);
        }
        return null;
    }

    /**
     * 读取文件内容
     *
     * @param fullFileName
     * @return 返回字符串list列表
     */
    public static List<String> readFileForApacheToList(String fullFileName, String encoding) throws IOException {

        if (StringUtils.isBlank(fullFileName)) {
            return null;
        }
        File file = new File(fullFileName);
        if (file != null) {
            return FileUtils.readLines(file, encoding);
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param fullFileName
     * @return 返回boolean
     */
    public static boolean delFileForApache(String fullFileName) {

        if (StringUtils.isBlank(fullFileName)) {
            return true;
        }
        File file = new File(fullFileName);
        if (file != null) {
            return FileUtils.deleteQuietly(file);
        }
        return true;
    }


    /**
     * 根据文件名创建文件
     *
     * @param fullFileName
     * @throws IOException
     */
    private static File createFile(String fullFileName) throws IOException {

        if (StringUtils.isBlank(fullFileName)) {
            return null;
        }
        // 截取文件夹路径
        int pointPosition = fullFileName.lastIndexOf("/");
        String path = fullFileName.substring(0, pointPosition);

        if (StringUtils.isNotBlank(path)) {
            File path_file = new File(path);
            if (!path_file.exists()) {
                path_file.mkdirs();
            }
        }

        File file = new File(fullFileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

}
