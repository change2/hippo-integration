package com.change.hippo.utils.encryption;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * User: change.long
 * Date: 2017/12/28
 * Time: 上午11:49
 */
public class TripleDES {
    private static final String DES_KEY_STRING = "Shanghai@#Telecom";
    private static String DES_KEY_STRING_MD5;
    private static final String MCRYPT_TRIPLEDES = "DESede";
    private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";

    private static final Logger logger = LoggerFactory.getLogger(TripleDES.class);

    public static String getDesKey(String key) {
        try {
            if (StringUtils.isBlank(DES_KEY_STRING_MD5)) {
                DES_KEY_STRING_MD5 = MD5Util.getMD5String(DES_KEY_STRING);
                DES_KEY_STRING_MD5 = DES_KEY_STRING_MD5.substring(0, 24);
            }
        } catch (NoSuchAlgorithmException e) {
            return key;
        }
        return DES_KEY_STRING_MD5;
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
            String encryptText = org.apache.commons.codec.binary.Base64.encodeBase64String(cipher.doFinal(text.getBytes()));
            return encryptText;
        } catch (Exception e) {
            logger.error("3des encrypt error. text: [{}], key: [{}]", text, key);
        }
        return null;
    }

    public static String encrypt(String text) {
        return encrypt(text, DES_KEY_STRING);
    }

    public static String decrypt(String encryptText, String key) {
        try {
            Cipher cipher = getCipher(key, false);
            return new String(cipher.doFinal(org.apache.commons.codec.binary.Base64.decodeBase64(encryptText)));
        } catch (Exception e) {
            logger.error("3des decrypt error. encryptText: [{}], key: [{}]", encryptText, key);
        }
        return null;
    }

    public static String decrypt(String encryptText) {
        return decrypt(encryptText, DES_KEY_STRING);
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String encryptText = encrypt("6220444023069538076;5dde32ac4b3f814ce8237ae612efcae0");
        System.out.println(encryptText);
        System.out.println("encrypt " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        System.out.println(decrypt("BNyK4chhU0upkLaYn6GLSYV0n32L/cZ7sHU6X3jSBcewyL05rtBTUkfoFvAZOhfKMohpfoIQKzA="));
        System.out.println("decrypt " + (System.currentTimeMillis() - start));
    }
}
