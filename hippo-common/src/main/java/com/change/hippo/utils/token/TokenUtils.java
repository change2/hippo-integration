package com.change.hippo.utils.token;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * User: change.long
 * Date: 2017/10/23
 * Time: 下午5:08
 */
public class TokenUtils {

    private static Logger logger = LoggerFactory.getLogger(TokenUtils.class);
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String CHARSET = "UTF-8";

    public static String create(String apiSecret, String data) {

        Key signingKey = new SecretKeySpec(apiSecret.getBytes(), HMAC_SHA1_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] rawData = data.getBytes(CHARSET);
            byte[] rawHmac = mac.doFinal(rawData);
            return new String(Base64.encodeBase64(rawHmac));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
