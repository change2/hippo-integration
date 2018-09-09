package com.change.hippo.utils.character;
public class DesensitizationUtil {
	/** 
     * 对字符串处理:将指定位置到指定位置的字符以星号代替 
     *  
     * @param content 
     *            传入的字符串 
     * @param begin 
     *            开始位置 
     * @param end 
     *            结束位置 
     * @return 
     */  
    public static String getStarString(String content, int begin, int end) {  
  
        if (begin >= content.length() || begin < 0) {  
            return content;  
        }  
        if (end >= content.length() || end < 0) {  
            return content;  
        }  
        if (begin >= end) {  
            return content;  
        }  
        String starStr = "";  
        for (int i = begin; i < end; i++) {  
            starStr = starStr + "*";  
        }  
        return content.substring(0, begin) + starStr + content.substring(end, content.length());  
  
    }  
      
    /** 
     * 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替 
     *  
     * @param content 
     *            传入的字符串 
     * @param frontNum 
     *            保留前面字符的位数 
     * @param endNum 
     *            保留后面字符的位数 
     * @return 带星号的字符串 
     */  
  
    public static String getStarString2(String content, int frontNum, int endNum) {  
  
        if (frontNum >= content.length() || frontNum < 0) {  
            return content;  
        }  
        if (endNum >= content.length() || endNum < 0) {  
            return content;  
        }  
        if (frontNum + endNum >= content.length()) {  
            return content;  
        }  
        String starStr = "";  
        for (int i = 0; i < (content.length() - frontNum - endNum); i++) {  
            starStr = starStr + "*";  
        }  
        return content.substring(0, frontNum) + starStr  
                + content.substring(content.length() - endNum, content.length());  
  
    }  
}
