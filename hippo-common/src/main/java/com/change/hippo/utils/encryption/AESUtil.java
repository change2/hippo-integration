package com.change.hippo.utils.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AESUtil {
    private static final String IV = "ZH9NC3Y5K9D0M7QL";
    private static Logger log = LoggerFactory.getLogger(AESUtil.class);
    // 加密
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            return null;
        }
        sKey = sKey.substring(0, 16);
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());

//		 return new BASE64Encoder().encode(encrypted);//
        // 此处使用BASE64做转码功能，同时能起到2次加密的作用。

//		return bytesToHex(encrypted);
        return Base64Encoder.encode(encrypted);
    }

    // 解密
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                return null;
            }
            sKey = sKey.substring(0, 16);
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

            /**
             * 这个地方调用BouncyCastleProvider
             *让java支持PKCS7Padding
             */
//            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());


            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//
            // 先用base64解密
//			byte[] encrypted1 = hexToBytes(sSrc);

            byte[] encrypted1 = Base64Decoder.decodeToBytes(sSrc);

            try {
                if (null != encrypted1){
                    byte[] original = cipher.doFinal(encrypted1);
                    String originalString = new String(original);
                    return originalString;
                }
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
     /*
	 * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
	 * 此处使用AES-128-CBC加密模式，key需要为16位。
	 */
        String cKey = "Shanghai@#Telecom";
        // 需要加密的字串
        String cSrc = "测试字符串";
        //log.info(MD5DigestUtil.getMD5String(cSrc));
        System.out.println(cSrc);
        // 加密
        long lStart = System.currentTimeMillis();
        String enString = AESUtil.Encrypt(cSrc, cKey);
        System.out.println("加密后的字串是：" + enString);

        long lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("加密耗时：" + lUseTime + "毫秒");
        // 解密
        lStart = System.currentTimeMillis();
        String DeString = AESUtil.Decrypt("6OCEIGg6lZ8rrcxarHCCRyW2YSqEFf0AstDfOUhVBZjrSQptN8lZZeynLBDkgAhfgZqkB+4Q/UYC\nAYpXem5fVQ== ", cKey);
        //String DeString = AESUtil.Decrypt("", Constant.EncodePassword.passwordKye);
        System.out.println("解密后的字串是：" + DeString);
        lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("解密耗时：" + lUseTime + "毫秒");
    }
}
