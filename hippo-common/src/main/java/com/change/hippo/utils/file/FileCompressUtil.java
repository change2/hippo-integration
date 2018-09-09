package com.change.hippo.utils.file;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * Created by mike on 16/6/15.
 */
public class FileCompressUtil {

    /**
     * 压缩
     * @param str 数据字节
     * @return
     * @throws IOException
     */
    public static byte[] deflateCompress(byte[] str) throws IOException {
        if (str == null || str.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DeflaterOutputStream deflate = new DeflaterOutputStream(out);
        deflate.write(str);
        deflate.close();
        return out.toByteArray();
    }

    /**
     * 解压缩
     * @param str
     * @return 返回byte[]
     * @throws IOException
     */
    public static byte[] inflateUncompress(byte[] str) throws IOException {
        if (str == null || str.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str);
        InflaterInputStream inflate = new InflaterInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = inflate.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * 解压缩
     * @param data
     * @return 返回字符串
     * @throws IOException
     * @throws DataFormatException
     */
    public static String inflateUncompressStr(byte[] data, String character) throws IOException, DataFormatException {
        if (StringUtils.isBlank(character)) {
            character = "UTF-8";
        }
        Inflater decompresser = new Inflater();
        decompresser.setInput(data);
        StringBuilder sb = new StringBuilder("");
        byte[] result = new byte[1000];
        int resultLength = 0;
        while ((resultLength = decompresser.inflate(result)) > 0) {
            sb.append(new String(result, 0, resultLength, character));
        }
        decompresser.end();
        return sb.toString();
    }
}
