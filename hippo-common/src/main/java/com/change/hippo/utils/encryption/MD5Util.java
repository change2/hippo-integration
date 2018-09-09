package com.change.hippo.utils.encryption;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mike on 16/6/1.
 */
public class MD5Util {
    /**
     * 实现MD5加密
     * @param inStr 需要加密的字符串
     * @return
     */
    /**
     * Default password digits
     */
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    protected static String MD5_DIGEST_NAME = "MD5";

    public final static String MD5(String inStr) {

        try {
            byte[] btInput = inStr.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance(MD5_DIGEST_NAME);
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成字符串的md5校验值
     *
     * @param s
     * @return
     */
    public static String getMD5String(String s) throws NoSuchAlgorithmException {
        return getMD5String(s.getBytes());
    }

    /**
     * Check MD5 matching
     *
     * @param password
     * @param md5PwdStr
     * @return
     */
    public static boolean checkMD5(String password, String md5PwdStr) throws NoSuchAlgorithmException {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    /**
     * Check MD5 matching
     *
     * @param sourceFile
     * @param md5Str
     * @return
     */
    public static boolean checkMD5(File sourceFile, String md5Str) {
        boolean f = false;
        String s;
        try {
            s = getFileMD5String(sourceFile);
            //System.out.println(new java.util.Date()+" md5 s:"+s);

            f = md5Str.equalsIgnoreCase(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;

    }

    /**
     * Check MD5 matching
     *
     * @param sourceFile
     * @param md5File
     * @return
     */
    public static boolean checkMD5(File sourceFile, File md5File) {
        //System.out.println(new java.util.Date()+" source File:"+sourceFile.getAbsolutePath());
        //System.out.println("md5File File:"+md5File.getAbsolutePath());
        boolean f = false;
        if (md5File.exists()) {
            String md5Str = "";
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(md5File));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().equals("")) {
                        md5Str = line.trim();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
            return checkMD5(sourceFile, md5Str);
        } else {

        }
        return f;


    }

    /**
     * generate the MD5 string by given file
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest messagedigest = MessageDigest.getInstance(MD5_DIGEST_NAME);
        InputStream fis;
        fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numRead = 0;
        while ((numRead = fis.read(buffer)) > 0) {
            messagedigest.update(buffer, 0, numRead);
        }
        try {
            fis.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return bufferToHex(messagedigest.digest());
    }

    public static String getMD5String(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest messagedigest = MessageDigest.getInstance(MD5_DIGEST_NAME);
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
