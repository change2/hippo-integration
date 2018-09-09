package com.change.hippo.utils.encryption;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Author: Tao.Wang
 * Date: 2018/5/2
 * Time: 下午3:31
 * Org : 思笛恩
 * To change this template use File | Settings | File Templates.
 * Description:
 *
 * @author: Tao.Wang
 */
public class TripleDESUtil {
    private static final Logger logger = LoggerFactory.getLogger(TripleDES.class);
    private static final String DES_KEY_STRING = "Shanghai@#Telecom";
    private static final String MCRYPT_TRIPLEDES = "DESede";
    private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";
    private static String MD5_DIGEST_NAME = "MD5";
    private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    //缓存key的md5值个数为200个
    private static LoadingCache<String, String> loadingCache = CacheBuilder
            .newBuilder()
            .maximumSize(100)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    if (StringUtils.isBlank(key)) {
                        key = DES_KEY_STRING;
                    }
                    String md5 = MD5(key);
                    try {
                        key = md5.substring(0, 24);
                    } catch (Exception ignore) {
                    }
                    return key;
                }

            });


    public static String getDesKey(String key) throws Exception {
        return loadingCache.get(key);
    }

    public static String MD5(String inStr) {
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
            return inStr;
        }
    }

    public static Cipher getCipher(String key, boolean enOrDe) {
        try {
            byte[] keyByte = getDesKey(key).getBytes();
            IvParameterSpec iv = new IvParameterSpec("00000000".getBytes());
            SecretKey secretKey = new SecretKeySpec(keyByte, MCRYPT_TRIPLEDES);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            if (enOrDe) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            }
            return cipher;
        } catch (Exception e) {
            logger.error("3des encrypt error. key: [{}]", key);
        }
        return null;
    }

    public static String encrypt(String text, String key) {
        try {
            Cipher cipher = getCipher(key, true);
            if (cipher != null) {
                byte[] binaryData = cipher.doFinal(text.getBytes());
                return org.apache.commons.codec.binary.Base64.encodeBase64String(binaryData);
            }
        } catch (Exception e) {
            logger.error("3des encrypt error. text: [{}], key: [{}]", text, key);
        }
        return null;
    }

    public static String decrypt(String encryptText, String key) {
        try {
            Cipher cipher = getCipher(key, false);
            if (null != cipher) {
                byte[] bytes = cipher.doFinal(Base64.decodeBase64(encryptText));
                return new String(bytes);
            }
        } catch (Exception e) {
            logger.error("3des decrypt error. encryptText: [{}], key: [{}]", encryptText, key);
        }
        return null;
    }


    public static void main(String[] args) throws InterruptedException {

        String password = "7b5e57aa682641a97d0474d6bae45bdc";
        long expireTime = TimeUnit.SECONDS.toMillis(60);//1分钟
        long l = System.currentTimeMillis() + expireTime;
        // 到期时间（毫秒）,光口号,
        String decrypt = TripleDES.encrypt(l + ",220000000019", password);
        System.out.println(decrypt);
        String result = TripleDES.decrypt(decrypt, password);

        if (null != result) {
            String expireArray[] = result.split(",");
            if (expireArray.length >= 2) {
                //得到当前时间
                TimeUnit.SECONDS.sleep(70);
                long current = System.currentTimeMillis();
                boolean expire = (current > Long.valueOf(expireArray[0]));
                System.out.println(expire);
            }
        }
    }
}
