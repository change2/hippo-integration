package com.change.hippo.utils.file;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by mike on 16/6/16.
 */
public class FileUploadUtil {

    private static final String character_encode = "utf-8";

    private static final int FILE_MAX_SIZE = 5000;

    /**
     * 获取上传文件的相关信息 适用于
     * @param request
     * @param fileMaxSize
     * @param encoding
     * @return
     * @throws FileUploadException
     */
    public static List<FileItem> requestFileItemForMultipart(HttpServletRequest request, Integer fileMaxSize, String encoding) throws FileUploadException {

        if (request == null) {
            return null;
        }
        if (fileMaxSize == null) {
            fileMaxSize = FILE_MAX_SIZE;
        }

        RequestContext reqContext = new ServletRequestContext(request);
        try {
            if (StringUtils.isBlank(encoding)) {
                encoding = character_encode;
            }
            request.setCharacterEncoding(encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (FileUpload.isMultipartContent(reqContext)) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload fileUpload = new ServletFileUpload(factory);
            fileUpload.setFileSizeMax(fileMaxSize);
            List<FileItem> itemsList = fileUpload.parseRequest(request);
            return itemsList;
        }
        return null;
    }
}
