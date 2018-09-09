package com.change.hippo.utils.env;

import java.util.HashMap;
import java.util.Map;

public class EnvHelpers {

	private EnvHelpers(){}

	private static final Map<String,String> caches = new HashMap<>();

	private static class Inner{
		private static EnvHelpers ins = new EnvHelpers();
	}

	public static EnvHelpers me(){
		return Inner.ins;
	}

	public String get(String key){
		return get(key,null);
	}

	public String get(String key,String defaultValue){
		String cache = caches.get(key);
		if(cache != null){
			return cache;
		}
		String value = System.getenv().get(key);
        if (value == null || "".equals(value)) {
            value = System.getProperty(key);
        }
		if(value!=null){
			cache = value.trim();
			caches.put(key,cache);
			return cache;
		}
		return defaultValue;
	}

	public boolean getBoolean(String key){
		return getBoolean(key,false);
	}

	public boolean getBoolean(String key,boolean defaultValue){
		String value = get(key);
		if(value == null){
			return defaultValue;
		}
		return Boolean.parseBoolean(value);
	}

	public int getInt(String key){
		return getInt(key,0);
	}

	public int getInt(String key,int defaultValue){
		String value = get(key);
		if(value == null){
			return defaultValue;
		}
		return Integer.parseInt(value);
	}

	public long getLong(String key){
		return getLong(key,0L);
	}

	public long getLong(String key,long defaultValue){
		String value = get(key);
		if(value == null){
			return defaultValue;
		}
		return Long.parseLong(value);
	}

	public double getDouble(String key){
		return getLong(key,0L);
	}

	public double getDouble(String key,double defaultValue){
		String value = get(key);
		if(value == null){
			return defaultValue;
		}
		return Double.parseDouble(value);
	}

}
