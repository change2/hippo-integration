package org.change.hippo.server.http;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WebAppUtil {
	private final static String SEPARATOR = "WEB-INF";
	private final static char PATH_SEPARATOR_CHAR = '/';
	private static String DEPLOY_NAME = null;
	private static String DEPLOY_NODE = null;

	public static String getDeployNode() {
		return DEPLOY_NODE;
	}

	public static String getDeployName() {
		return DEPLOY_NAME;
	}

	private static char[] listToArray(List<Character> list) {
		char[] result = new char[list.size()];
		int index = 0;
		Iterator<Character> it = list.iterator();
		while (it.hasNext()) {
			char c = it.next();
			result[index++] = c;
		}
		return result;
	}

	public static void initDeployInfo() throws UnknownHostException {
		RuntimeMXBean mxbean = ManagementFactory.getRuntimeMXBean();
		for (String kvpairs : mxbean.getInputArguments()) {
			String[] kv = kvpairs.split("=");
			if (kv.length != 2) {
				continue;
			}
			String key = kv[0];
			String value = kv[1];
			if ("-Dcatalina.base".equals(key)) {
				DEPLOY_NODE = value.substring(value.lastIndexOf(File.separator) + 1);
			}
		}

		if (DEPLOY_NODE == null) {
			//获取tomcat节点信息
			URL url = WebAppUtil.class.getResource("/");
			String classPath = url.toString();
			String[] segments = classPath.split(String.valueOf(PATH_SEPARATOR_CHAR));
			for (int i = 0; i < segments.length; i++) {
				if (SEPARATOR.equals(segments[i]) && i >= 3) {
					DEPLOY_NODE = segments[i - 3];
				}
			}
		}
		URL url = WebAppUtil.class.getResource("/");
		String classPath = url.toString();
		char[] value = classPath.toCharArray();
		char[] sepatatorAry = SEPARATOR.toCharArray();
		List<Character> temp = new LinkedList<Character>();
		char[] result = null;
		for (char c : value) {
			if (c == PATH_SEPARATOR_CHAR) {
				char[] segment = listToArray(temp);
				if (Arrays.equals(segment, sepatatorAry)) {
					DEPLOY_NAME = new String(result);
				}
				result = segment;
				temp.clear();
			} else {
				temp.add(c);
			}
		}

	}
}
