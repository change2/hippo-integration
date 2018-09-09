package org.change.hippo.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;


public class JsonUtils {

	private static ObjectMapper MAPPER = new ObjectMapper();
	
	static{
		MAPPER.disable(FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public static <T> T readValue(String json, Class<T> clazz) throws Exception {
		return MAPPER.readValue(json, clazz);
	}

	public static String writeValueAsString(Object object) throws Exception {
		return MAPPER.writeValueAsString(object);
	}

	public static boolean isArray(String json) throws Exception {
		return List.class.isAssignableFrom(readValue(json, Object.class).getClass());
	}

	public static boolean isObject(String json) throws Exception {
		return Map.class.isAssignableFrom(readValue(json, Object.class).getClass());
	}

	public static <E> List<E> readList(String json, Class<E> clazz) throws Exception {
		return MAPPER.readValue(json, MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
	}

}
