package org.change.hippo.server.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * User: change.long
 * Date: 2017/11/21
 * Time: 下午3:59
 */
public class SignatureUtils {
    private static Logger logger = LoggerFactory.getLogger(SignatureUtils.class);
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String CHARSET = "UTF-8";

    public static String signature(final String apiSecret, final String postData) {
        Key signingKey = new SecretKeySpec(apiSecret.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac;
        try {
            mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] rawData = postData.getBytes(CHARSET);
            byte[] rawHmac = mac.doFinal(rawData);
            //System.out.println(rawHmac);
            return new String(Base64.encodeBase64((rawHmac)));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static void main(String[] args) {
        String aes = "{\"sign\":\"eed08308955730a1ef2ef2304ae9f3a2\",\"encodeString\":\"f+sPThDUewxaCtPOkyU8zHQa8qS8GiO3Ik46cQbIurW5kf5bkis8+Zojf1A3crnC\"}";
        System.out.println(SignatureUtils.signature("d1fc1c5cf71169e824a86eead0ed19c0",aes));
    }
}
