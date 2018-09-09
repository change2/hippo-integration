package com.change.hippo.utils.http.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class MacUtil {
	 //获取mac
	public static String getMac(String ip) {
		String line = "";
		String macAddress = "";
		final String MAC_ADDRESS_PREFIX = "MAC Address = ";
		final String LOOPBACK_ADDRESS = "127.0.0.1";
		// 如果为127.0.0.1,则获取本地MAC地址。
		try {
			if (LOOPBACK_ADDRESS.equals(ip)) {
				InetAddress inetAddress = InetAddress.getLocalHost();
				// 貌似此方法需要JDK1.6。
				byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
				// 下面代码是把mac地址拼装成String
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < mac.length; i++) {
					if (i != 0) {
						sb.append("-");
					}
					// mac[i] & 0xFF 是为了把byte转化为正整数
					String s = Integer.toHexString(mac[i] & 0xFF);
					sb.append(s.length() == 1 ? 0 + s : s);
				}
				// 把字符串所有小写字母改为大写成为正规的mac地址并返回
				macAddress = sb.toString().trim().toUpperCase();
				return macAddress;
			}
			// 获取非本地IP的MAC地址
			Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
			InputStreamReader isr = new InputStreamReader(p.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line != null) {
					int index = line.indexOf(MAC_ADDRESS_PREFIX);
					if (index != -1) {
						macAddress = line.substring(index + MAC_ADDRESS_PREFIX.length()).trim().toUpperCase();
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return macAddress;
	}
}
