package com.change.hippo.utils.kafka;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import com.change.hippo.utils.ri.RIDConst;
import com.change.hippo.utils.ri.RequestIdentityHolder;
import com.change.hippo.utils.ri.RequestInfo;

import java.util.Iterator;

/**
 * User: change.long
 * Date: 2017/12/11
 * Time: 下午7:20
 */
public class KafkaInvocationHandler {

    public static void supportRequestIdentity(Headers headers) {
        String rid = null, name = null, version = null, step = "0";
        Iterator<Header> iterator = headers.headers(RIDConst.RID).iterator();
        if (iterator.hasNext()) {
            Header ridHeader = iterator.next();
            if (ridHeader != null) {
                byte[] value = ridHeader.value();
                if (value != null && value.length > 0) {
                    rid = StringUtils.toEncodedString(value, null);
                }
            }
        } else {
            rid = RequestIdentityHolder.generateRid(); // 如果调用方未传递,则自动生成
            name = RequestIdentityHolder.getName();
            version = RequestIdentityHolder.getVersion();
            step = "0";
        }
        RequestInfo requestInfo = RequestIdentityHolder.get(true);
        requestInfo.setId(rid);
        requestInfo.setStep(Integer.valueOf(step) + 1);
        requestInfo.setName(name);
        requestInfo.setVersion(version);
        RequestIdentityHolder.set(requestInfo);
    }

    public static Header setRequestIdentity() {
        RequestInfo requestInfo = RequestIdentityHolder.get();
        if (requestInfo != null) {
            String xId = requestInfo.getId();
            byte[] xIdToUse = xId.getBytes();
            return new RecordHeader(RIDConst.RID, xIdToUse);
        }
        return null;
    }

    public static String key(ConsumerRecord<String, String> record) {
        if (record == null) {
            return null;
        }
        String key = record.key();
        if (null != key && !key.isEmpty()) {
            return key;
        }
        return record.partition() + "";
    }

    public static int signSafeMod(long dividend, int divisor) {
        int mod = (int) (dividend % divisor);

        if (mod < 0) {
            mod += divisor;
        }

        return mod;
    }

    public static void main(String[] args) {
        System.out.println(signSafeMod(-14L, 10));
    }
}
