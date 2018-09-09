package com.change.hippo.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * 采用阿里开源的fastjson
 * <p>原有的common-utils包中已存在json工具包，采用的是google的Gson</p>
 * @author:fulong
 * @create:2017/8/1 15:40.
 */
public class JsonHelpers {

	private JsonHelpers(){}

	private static class Inner {
		private static final JsonHelpers ins = new JsonHelpers();
	}

	public static JsonHelpers me(){
		return Inner.ins;
	}

	public String toJson(Object instance){
		return JSON.toJSONString(instance);
	}

	public <T> T getBean(String text,Class<T> clazz){
		return JSON.parseObject(text, clazz);
	}

	public <T> List<T> getBeans(String text, Class<T> clazz){
		return JSON.parseArray(text, clazz);
	}

	public List<Map<String,Object>> getListMap(String text){
		return JSON.parseObject(text, new TypeReference<List<Map<String, Object>>>() {});
	}

	/**
	 * 美化输出
	 */
	public String prettyPrint(String json){
		return new JSONFormat(json).format();
	}

	class JSONFormat{
		private String src;
		private int TABLength = 0;
		private final String BRACKET_LEFT = "[";
		private final String BRACKET_RIGHT = "]";
		private final String BRACE_LEFT = "{";
		private final String BRACE_RIGHT = "}";
		private final String COMMA = ",";
		private final String LINE_BREAK = "\r\n";
		private final String TAB = "\t";

		public JSONFormat(String src){
			this.src = src;
		}

		public String format() throws JSONException {
			try {
				JSONObject.parseObject(src);
			} catch (JSONException e) {
				//对JSON格式进行简单的检验
				throw e;
			}
			return format(src);
		}

		private String format(String json){
			StringBuilder result = new StringBuilder();
			char[] srcArray = json.toCharArray();
			for (int index = 0; index < src.length(); index++) {
				result.append(srcArray[index]);
				if (BRACE_LEFT.equals(String.valueOf(srcArray[index])))  //{
					result.append(appendLINE_BREAKAndTAB(++TABLength));
				if (BRACE_RIGHT.equals(String.valueOf(srcArray[index]))) //}
					result.insert(result.length() - 1, appendLINE_BREAKAndTAB(--TABLength));
				if (BRACKET_LEFT.equals(String.valueOf(srcArray[index])))    //[
					result.append(appendLINE_BREAKAndTAB(++TABLength));
				if (BRACKET_RIGHT.equals(String.valueOf(srcArray[index])))   //]
					result.insert(result.length() - 1, appendLINE_BREAKAndTAB(--TABLength));
				if (COMMA.equals(String.valueOf(srcArray[index])))   //,
					result.append(appendLINE_BREAKAndTAB(TABLength));
			}
			return result.toString();
		}

		//追加换行符和   确定长度的制表符
		private String appendLINE_BREAKAndTAB(int TABTimes) {
			StringBuilder temp = new StringBuilder();
			temp.append(appendLINE_BREAK());
			temp.append(appendTAB(TABTimes));
			return temp.toString();
		}

		private String appendLINE_BREAK() {
			return LINE_BREAK;
		}

		private String appendTAB(int TABTimes) {
			StringBuilder temp = new StringBuilder();
			for (int i = 0; i < TABTimes; i++) {
				temp.append(TAB);
			}
			return temp.toString();
		}

	}

}
